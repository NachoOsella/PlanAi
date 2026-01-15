package com.planai.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planai.model.dto.request.ChatRequest;
import com.planai.model.dto.response.ChatResponse;
import com.planai.model.dto.response.ConversationResponse;
import com.planai.model.dto.response.ProjectDetailResponse;
import com.planai.mapper.ConversationMapper;
import com.planai.repository.ConversationRepository;
import com.planai.service.AiService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for AI-powered chat and plan extraction endpoints.
 */
@RestController
@RequestMapping("/api/v1/projects/{projectId}")
@Tag(name = "AI", description = "AI chat and plan extraction endpoints")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final ConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;

    /**
     * Send a message to the AI planning assistant and receive a response.
     * If conversationId is not provided, a new conversation will be created.
     *
     * @param projectId The ID of the project for this conversation.
     * @param request   The chat request containing the message and optional conversation ID.
     * @return ChatResponse containing the conversation ID and both user and assistant messages.
     */
    @PostMapping("/chat")
    @Operation(
            summary = "Send a message to the AI planning assistant",
            description = "Sends a user message to the AI and returns the assistant's response. " +
                    "If no conversationId is provided, a new conversation is created."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Message processed successfully"),
            @ApiResponse(responseCode = "404", description = "Project or conversation not found"),
            @ApiResponse(responseCode = "503", description = "AI service unavailable or error")
    })
    public ResponseEntity<ChatResponse> chat(
            @Parameter(description = "The project ID") @PathVariable Long projectId,
            @Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(aiService.chat(projectId, request));
    }

    /**
     * Get all conversations for a project.
     *
     * @param projectId The ID of the project.
     * @return List of conversations with their messages.
     */
    @GetMapping("/conversations")
    @Operation(
            summary = "Get all conversations for a project",
            description = "Returns all conversations associated with a project, including their messages."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conversations retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<List<ConversationResponse>> getConversations(
            @Parameter(description = "The project ID") @PathVariable Long projectId) {
        List<ConversationResponse> conversations = conversationRepository
                .findByProjectIdOrderByCreatedAtDesc(projectId)
                .stream()
                .map(conv -> {
                    // Load full conversation with messages
                    return conversationRepository.findById(conv.getId())
                            .map(conversationMapper::toResponse)
                            .orElse(null);
                })
                .filter(conv -> conv != null)
                .collect(Collectors.toList());

        return ResponseEntity.ok(conversations);
    }

    /**
     * Extract a structured plan from all conversations in a project.
     * This will analyze the conversation history and create epics, user stories, and tasks.
     * Note: This replaces any existing plan (epics, stories, tasks) in the project.
     *
     * @param projectId The ID of the project to extract the plan for.
     * @return The updated project details including the generated plan.
     */
    @PostMapping("/extract-plan")
    @Operation(
            summary = "Extract a structured plan from conversations",
            description = "Analyzes all conversation history for the project and generates a structured plan " +
                    "with epics, user stories, and tasks. WARNING: This replaces any existing plan."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plan extracted successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "503", description = "AI service unavailable or no conversations to analyze")
    })
    public ResponseEntity<ProjectDetailResponse> extractPlan(
            @Parameter(description = "The project ID") @PathVariable Long projectId) {
        return ResponseEntity.ok(aiService.extractPlan(projectId));
    }
}
