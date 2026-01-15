package com.planai.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planai.exception.AiGenerationException;
import com.planai.exception.ResourceNotFoundException;
import com.planai.mapper.ProjectMapper;
import com.planai.model.dto.request.ChatRequest;
import com.planai.model.dto.response.ChatResponse;
import com.planai.model.dto.response.ProjectDetailResponse;
import com.planai.model.entity.ConversationEntity;
import com.planai.model.entity.EpicEntity;
import com.planai.model.entity.MessageEntity;
import com.planai.model.entity.ProjectEntity;
import com.planai.model.entity.TaskEntity;
import com.planai.model.entity.UserStoryEntity;
import com.planai.model.enums.MessageRoleEnum;
import com.planai.model.enums.PriorityEnum;
import com.planai.model.enums.StatusEnum;
import com.planai.repository.ConversationRepository;
import com.planai.repository.MessageRepository;
import com.planai.repository.ProjectRepository;
import com.planai.service.AiService;

/**
 * Implementation of {@link AiService} using Spring AI.
 */
@Service
public class AiServiceImpl implements AiService {

    private static final String PLANNING_PROMPT_PATH = "classpath:prompts/planning-assistant.md";
    private static final String STRUCTURE_PROMPT_PATH = "classpath:prompts/structure-plan.md";

    private final ChatClient chatClient;
    private final ProjectRepository projectRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final ProjectMapper projectMapper;
    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    public AiServiceImpl(ChatClient chatClient, ProjectRepository projectRepository,
            ConversationRepository conversationRepository, MessageRepository messageRepository,
            ProjectMapper projectMapper, ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.chatClient = chatClient;
        this.projectRepository = projectRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.projectMapper = projectMapper;
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
    }

    @Override
    @Transactional
    public ChatResponse chat(Long projectId, ChatRequest request) {
        ProjectEntity project = projectRepository.findByIdWithHierarchy(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));
        ConversationEntity conversation = resolveConversation(project, request.getConversationId());
        List<MessageEntity> contextMessages = getRecentMessages(conversation.getId());

        MessageEntity userMessage = saveMessage(conversation, MessageRoleEnum.USER, request.getMessage());

        String systemPrompt = buildSystemPrompt(project);
        List<Message> promptMessages = buildPromptMessages(systemPrompt, contextMessages, request.getMessage());
        String assistantResponse = callAssistant(promptMessages);

        saveMessage(conversation, MessageRoleEnum.ASSISTANT, assistantResponse);

        return ChatResponse.builder()
                .conversationId(conversation.getId())
                .userMessage(userMessage.getContent())
                .assistantMessage(assistantResponse)
                .build();
    }

    @Override
    @Transactional
    public ProjectDetailResponse extractPlan(Long projectId) {
        ProjectEntity project = projectRepository.findByIdWithHierarchy(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));

        List<ConversationEntity> conversations = conversationRepository.findByProjectIdOrderByCreatedAtDesc(projectId);
        if (conversations.isEmpty()) {
            throw new AiGenerationException("No conversations available to extract a plan.");
        }

        List<ConversationEntity> orderedConversations = new ArrayList<>(conversations);
        Collections.reverse(orderedConversations);
        String conversationHistory = buildConversationHistory(orderedConversations);
        if (conversationHistory.isBlank()) {
            throw new AiGenerationException("No messages available to extract a plan.");
        }

        String fullContext = buildProjectContext(project) + "\n\n" + conversationHistory;
        String promptTemplate = loadPrompt(STRUCTURE_PROMPT_PATH);
        String promptText = promptTemplate.replace("{{conversation_history}}", fullContext);

        String aiResponse = callAssistant(List.of(new SystemMessage(promptText)));
        JsonNode rootNode = parseJson(aiResponse);
        applyPlan(project, rootNode);

        ProjectEntity savedProject = projectRepository.save(project);
        return projectMapper.toDetailResponse(savedProject);
    }

    private ConversationEntity resolveConversation(ProjectEntity project, Long conversationId) {
        if (conversationId == null) {
            ConversationEntity conversation = ConversationEntity.builder().project(project).build();
            return conversationRepository.save(conversation);
        }

        ConversationEntity conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", conversationId));

        if (!conversation.getProject().getId().equals(project.getId())) {
            throw new ResourceNotFoundException("Conversation", conversationId);
        }

        return conversation;
    }

    private List<MessageEntity> getRecentMessages(Long conversationId) {
        List<MessageEntity> recentMessages =
                messageRepository.findTop10ByConversationIdOrderByCreatedAtDesc(conversationId);
        Collections.reverse(recentMessages);
        return recentMessages;
    }

    private MessageEntity saveMessage(ConversationEntity conversation, MessageRoleEnum role, String content) {
        MessageEntity message = MessageEntity.builder()
                .conversation(conversation)
                .role(role)
                .content(content)
                .build();
        return messageRepository.save(message);
    }

    private String buildSystemPrompt(ProjectEntity project) {
        String promptTemplate = loadPrompt(PLANNING_PROMPT_PATH);
        String context = buildProjectContext(project);
        return promptTemplate + "\n\n# Current Project Context\n" + context;
    }

    private String buildProjectContext(ProjectEntity project) {
        StringBuilder builder = new StringBuilder();
        builder.append("## Project\n");
        builder.append("- Name: ").append(project.getName()).append("\n");
        if (project.getDescription() != null && !project.getDescription().isBlank()) {
            builder.append("- Description: ").append(project.getDescription()).append("\n");
        }

        List<EpicEntity> epics = project.getEpics() == null ? new ArrayList<>() : new ArrayList<>(project.getEpics());
        epics.sort(Comparator.comparing(EpicEntity::getOrderIndex, Comparator.nullsLast(Integer::compareTo)));

        builder.append("\n## Epics\n");
        if (epics.isEmpty()) {
            builder.append("- No epics defined yet.\n");
            return builder.toString();
        }

        for (EpicEntity epic : epics) {
            builder.append("- Epic: ").append(epic.getTitle());
            builder.append(" (Priority: ").append(epic.getPriority()).append(", Status: ")
                    .append(epic.getStatus()).append(")\n");
            if (epic.getDescription() != null && !epic.getDescription().isBlank()) {
                builder.append("  - Description: ").append(epic.getDescription()).append("\n");
            }

            List<UserStoryEntity> stories = epic.getStories() == null
                    ? new ArrayList<>()
                    : new ArrayList<>(epic.getStories());
            stories.sort(Comparator.comparing(UserStoryEntity::getOrderIndex, Comparator.nullsLast(Integer::compareTo)));
            if (stories.isEmpty()) {
                builder.append("  - No user stories defined yet.\n");
                continue;
            }

            for (UserStoryEntity story : stories) {
                builder.append("  - Story: ").append(story.getTitle());
                builder.append(" (Priority: ").append(story.getPriority()).append(", Status: ")
                        .append(story.getStatus()).append(")\n");
                if (story.getAsA() != null && !story.getAsA().isBlank()) {
                    builder.append("    - As a: ").append(story.getAsA()).append("\n");
                }
                if (story.getIWant() != null && !story.getIWant().isBlank()) {
                    builder.append("    - I want: ").append(story.getIWant()).append("\n");
                }
                if (story.getSoThat() != null && !story.getSoThat().isBlank()) {
                    builder.append("    - So that: ").append(story.getSoThat()).append("\n");
                }

                List<TaskEntity> tasks = story.getTasks() == null
                        ? new ArrayList<>()
                        : new ArrayList<>(story.getTasks());
                tasks.sort(Comparator.comparing(TaskEntity::getOrderIndex, Comparator.nullsLast(Integer::compareTo)));
                if (tasks.isEmpty()) {
                    builder.append("    - No tasks defined yet.\n");
                    continue;
                }

                for (TaskEntity task : tasks) {
                    builder.append("    - Task: ").append(task.getTitle());
                    builder.append(" (Status: ").append(task.getStatus());
                    if (task.getEstimatedHours() != null) {
                        builder.append(", Est: ").append(task.getEstimatedHours()).append("h");
                    }
                    builder.append(")\n");
                    if (task.getDescription() != null && !task.getDescription().isBlank()) {
                        builder.append("      - Description: ").append(task.getDescription()).append("\n");
                    }
                }
            }
        }

        return builder.toString();
    }

    private List<Message> buildPromptMessages(String systemPrompt, List<MessageEntity> history, String userMessage) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(systemPrompt));
        for (MessageEntity message : history) {
            Message chatMessage = toChatMessage(message);
            if (chatMessage != null) {
                messages.add(chatMessage);
            }
        }
        messages.add(new UserMessage(userMessage));
        return messages;
    }

    private Message toChatMessage(MessageEntity message) {
        if (message.getRole() == MessageRoleEnum.USER) {
            return new UserMessage(message.getContent());
        }
        if (message.getRole() == MessageRoleEnum.ASSISTANT) {
            return new AssistantMessage(message.getContent());
        }
        if (message.getRole() == MessageRoleEnum.SYSTEM) {
            return new SystemMessage(message.getContent());
        }
        return null;
    }

    private String callAssistant(List<Message> promptMessages) {
        try {
            String response = chatClient.prompt(new Prompt(promptMessages)).call().content();
            if (response == null || response.isBlank()) {
                throw new AiGenerationException("AI response was empty.");
            }
            return response;
        } catch (Exception exception) {
            if (exception instanceof AiGenerationException) {
                throw exception;
            }
            throw new AiGenerationException("Failed to generate AI response.", exception);
        }
    }

    private String buildConversationHistory(List<ConversationEntity> conversations) {
        StringBuilder builder = new StringBuilder();
        for (ConversationEntity conversation : conversations) {
            List<MessageEntity> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversation.getId());
            if (messages.isEmpty()) {
                continue;
            }
            builder.append("Conversation ").append(conversation.getId()).append(":\n");
            for (MessageEntity message : messages) {
                builder.append(message.getRole()).append(": ").append(message.getContent()).append("\n");
            }
            builder.append("\n");
        }
        return builder.toString().trim();
    }

    private JsonNode parseJson(String aiResponse) {
        String jsonPayload = extractJson(aiResponse);
        try {
            ObjectMapper mapper = objectMapper.copy();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readTree(jsonPayload);
        } catch (IOException exception) {
            throw new AiGenerationException("Failed to parse AI response JSON.", exception);
        }
    }

    private String extractJson(String response) {
        int startIndex = response.indexOf('{');
        int endIndex = response.lastIndexOf('}');
        if (startIndex == -1 || endIndex == -1 || endIndex < startIndex) {
            throw new AiGenerationException("AI response did not contain valid JSON.");
        }
        return response.substring(startIndex, endIndex + 1).trim();
    }

    private void applyPlan(ProjectEntity project, JsonNode rootNode) {
        project.getEpics().clear();

        JsonNode epicsNode = rootNode.path("epics");
        if (!epicsNode.isArray()) {
            throw new AiGenerationException("AI response did not include epics array.");
        }

        int epicIndex = 0;
        for (JsonNode epicNode : epicsNode) {
            EpicEntity epic = new EpicEntity();
            epic.setTitle(defaultString(epicNode.path("title").asText(null), "Untitled Epic"));
            epic.setDescription(epicNode.path("description").asText(null));
            epic.setPriority(resolvePriority(epicNode.path("priority").asText(null)));
            epic.setStatus(StatusEnum.TODO);
            epic.setOrderIndex(epicIndex++);
            project.addEpic(epic);

            JsonNode storiesNode = epicNode.path("userStories");
            int storyIndex = 0;
            if (storiesNode.isArray()) {
                for (JsonNode storyNode : storiesNode) {
                    UserStoryEntity story = new UserStoryEntity();
                    story.setTitle(defaultString(storyNode.path("title").asText(null), "Untitled Story"));
                    story.setAsA(storyNode.path("asA").asText(null));
                    story.setIWant(storyNode.path("iWant").asText(null));
                    story.setSoThat(storyNode.path("soThat").asText(null));
                    story.setPriority(resolvePriority(storyNode.path("priority").asText(null)));
                    story.setStatus(StatusEnum.TODO);
                    story.setOrderIndex(storyIndex++);
                    epic.addStory(story);

                    JsonNode tasksNode = storyNode.path("tasks");
                    int taskIndex = 0;
                    if (tasksNode.isArray()) {
                        for (JsonNode taskNode : tasksNode) {
                            TaskEntity task = new TaskEntity();
                            task.setTitle(defaultString(taskNode.path("title").asText(null), "Untitled Task"));
                            task.setDescription(taskNode.path("description").asText(null));
                            task.setEstimatedHours(resolveEstimatedHours(taskNode.path("estimatedHours").asInt()));
                            task.setStatus(StatusEnum.TODO);
                            task.setOrderIndex(taskIndex++);
                            story.addTask(task);
                        }
                    }
                }
            }
        }
    }

    private PriorityEnum resolvePriority(String value) {
        if (value == null || value.isBlank()) {
            return PriorityEnum.MEDIUM;
        }
        try {
            return PriorityEnum.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException exception) {
            return PriorityEnum.MEDIUM;
        }
    }

    private Integer resolveEstimatedHours(int value) {
        if (value <= 0) {
            return 4;
        }
        return value;
    }

    private String defaultString(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value;
    }

    private String loadPrompt(String path) {
        Resource resource = resourceLoader.getResource(path);
        try (var inputStream = resource.getInputStream()) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8).trim();
        } catch (IOException exception) {
            throw new AiGenerationException("Failed to load prompt template: " + path, exception);
        }
    }
}
