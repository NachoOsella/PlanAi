package com.planai.service.impl;

import com.planai.config.GenAiConfig;
import com.planai.model.dto.request.ChatRequest;
import com.planai.model.dto.response.ChatResponse;
import com.planai.model.dto.response.ProjectDetailResponse;
import com.planai.service.AiService;

/**
 * AiServiceImpl
 */
public class AiServiceImpl implements AiService {

    private final GenAiConfig genAiConfig;

    public AiServiceImpl(GenAiConfig genAiConfig) {
        this.genAiConfig = genAiConfig;
    }

    @Override
    public ChatResponse chat(Long projectId, ChatRequest request) {
        return null;
    }

    @Override
    public ProjectDetailResponse extractPlan(Long projectId) {
        return null;
    }

}
