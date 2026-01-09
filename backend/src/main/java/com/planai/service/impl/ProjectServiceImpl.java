package com.planai.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planai.model.dto.request.CreateProjectRequest;
import com.planai.model.dto.request.UpdateProjectRequest;
import com.planai.model.dto.response.ProjectDetailResponse;
import com.planai.model.dto.response.ProjectResponse;
import com.planai.service.ProjectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link ProjectService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    @Override
    public List<ProjectResponse> getAllProjects() {
        return List.of();
    }

    @Override
    public ProjectDetailResponse getProjectDetail(Long projectId) {
        return null;
    }

    @Override
    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request) {
        return null;
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Long projectId, UpdateProjectRequest request) {
        return null;
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        // Implementation
    }
}
