package com.planai.service.interfaces;

import java.util.List;

import com.planai.model.dto.request.CreateProjectRequest;
import com.planai.model.dto.request.UpdateProjectRequest;
import com.planai.model.dto.response.ProjectDetailResponse;
import com.planai.model.dto.response.ProjectResponse;

/**
 * Interface for Project Service defining project-related operations.
 */
public interface IProjectService {

    /**
     * Retrieves a list of all projects.
     *
     * @return List of ProjectResponse objects.
     */
    List<ProjectResponse> getAllProjects();

    /**
     * Retrieves detailed information about a specific project by its ID.
     *
     * @param projectId The ID of the project.
     * @return ProjectDetailResponse object containing detailed project information.
     */
    ProjectDetailResponse getProjectDetail(Long projectId);

    /**
     * Creates a new project.
     *
     * @param request The CreateProjectRequest object containing project creation details.
     * @return ProjectResponse object representing the created project.
     */
    ProjectResponse createProject(CreateProjectRequest request);

    /**
     * Updates an existing project.
     *
     * @param projectId The ID of the project to update.
     * @param request The UpdateProjectRequest object containing updated project details.
     * @return ProjectResponse object representing the updated project.
     */
    ProjectResponse updateProject(Long projectId, UpdateProjectRequest request);

    /**
     * Deletes a project by its ID.
     *
     * @param projectId The ID of the project to delete.
     */
    void deleteProject(Long projectId);
}
