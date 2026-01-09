package com.planai.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planai.model.dto.request.CreateEpicRequest;
import com.planai.model.dto.response.EpicResponse;
import com.planai.service.EpicService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link EpicService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EpicServiceImpl implements EpicService {

    @Override
    public List<EpicResponse> getProjectEpics(Long projectId) {
        return List.of();
    }

    @Override
    @Transactional
    public EpicResponse createEpic(Long projectId, CreateEpicRequest request) {
        return null;
    }

    @Override
    @Transactional
    public EpicResponse updateEpic(Long epicId, CreateEpicRequest request) {
        return null;
    }

    @Override
    @Transactional
    public void deleteEpic(Long epicId) {
        // Implementation
    }

    @Override
    @Transactional
    public void reorderEpics(Long projectId, List<Long> epicIds) {
        // Implementation
    }
}
