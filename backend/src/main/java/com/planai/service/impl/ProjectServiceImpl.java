package com.planai.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planai.mapper.ProjectMapper;
import com.planai.model.dto.request.CreateProjectRequest;
import com.planai.model.dto.request.UpdateProjectRequest;
import com.planai.model.dto.response.ProjectDetailResponse;
import com.planai.model.dto.response.ProjectResponse;
import com.planai.model.entity.ProjectEntity;
import com.planai.repository.ProjectRepository;
import com.planai.service.ProjectService;

import com.planai.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link ProjectService}.
 * Handles project-related business logic.
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    /**
     * Retrieves all projects from the database.
     *
     * @return List of ProjectResponse objects containing project summaries.
     */
    @Override
    public List<ProjectResponse> getAllProjects() {
        List<ProjectEntity> projectEntities = projectRepository.findAllByOrderByCreatedAtDesc();
        return projectEntities.stream().map(projectMapper::toResponse).toList();
    }

    /**
     * Retrieves detailed information about a specific project including its epics.
     *
     * @param projectId The ID of the project to retrieve.
     * @return ProjectDetailResponse containing full project details with nested epics.
     * @throws ResourceNotFoundException if the project does not exist.
     */
    @Override
    public ProjectDetailResponse getProjectDetail(Long projectId) {
        ProjectEntity projectEntity = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));
        return projectMapper.toDetailResponse(projectEntity);
    }

    /**
     * Creates a new project in the database.
     *
     * @param request The CreateProjectRequest containing project creation details.
     * @return ProjectResponse representing the created project.
     */
    @Override
    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request) {
        ProjectEntity projectEntity = projectMapper.toEntity(request);
        ProjectEntity savedEntity = projectRepository.save(projectEntity);
        return projectMapper.toResponse(savedEntity);
    }

    /**
     * Updates an existing project's details.
     *
     * @param projectId The ID of the project to update.
     * @param request The UpdateProjectRequest containing updated project details.
     * @return ProjectResponse representing the updated project.
     * @throws ResourceNotFoundException if the project does not exist.
     */
    @Override
    @Transactional
    public ProjectResponse updateProject(Long projectId, UpdateProjectRequest request) {
        ProjectEntity projectEntity = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));
        projectMapper.updateEntity(request, projectEntity);
        ProjectEntity updatedEntity = projectRepository.save(projectEntity);
        return projectMapper.toResponse(updatedEntity);
    }

    /**
     * Deletes a project by its ID.
     *
     * @param projectId The ID of the project to delete.
     * @throws ResourceNotFoundException if the project does not exist.
     */
    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project", projectId);
        }
        projectRepository.deleteById(projectId);
    }
}
