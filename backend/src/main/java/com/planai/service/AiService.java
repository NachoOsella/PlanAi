package com.planai.service;

import com.planai.model.dto.request.ChatRequest;
import com.planai.model.dto.response.ChatResponse;
import com.planai.model.dto.response.ProjectDetailResponse;

/**
 * Service for handling AI interactions using Spring AI.
 */
public interface AiService {

    /**
     * Process a user message and return the AI's response.
     *
     * @param projectId The ID of the project this chat belongs to.
     * @param request   The chat request containing the message and optional
     *                  conversation ID.
     * @return The updated conversation with the new messages.
     */
    ChatResponse chat(Long projectId, ChatRequest request);

    /**
     * Analyze the conversation history for a project and generate/update the
     * project plan.
     *
     * @param projectId The ID of the project to extract the plan for.
     * @return The updated project details including the generated Epics, Stories,
     *         and Tasks.
     */
    ProjectDetailResponse extractPlan(Long projectId);
}
