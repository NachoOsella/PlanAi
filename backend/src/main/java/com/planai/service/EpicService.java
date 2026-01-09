package com.planai.service;

import java.util.List;

import com.planai.model.dto.request.CreateEpicRequest;
import com.planai.model.dto.response.EpicResponse;

/**
 * Interface for Epic Service defining epic-related operations.
 */
public interface EpicService {

    /**
     * Retrieves all epics associated with a specific project.
     *
     * @param projectId The ID of the project.
     * @return List of EpicResponse objects.
     */
    List<EpicResponse> getProjectEpics(Long projectId);

    /**
     * Creates a new epic within a project.
     *
     * @param projectId The ID of the project where the epic will be created.
     * @param request The CreateEpicRequest object containing epic creation details.
     * @return EpicResponse object representing the created epic.
     */
    EpicResponse createEpic(Long projectId, CreateEpicRequest request);

    /**
     * Updates an existing epic's details.
     *
     * @param epicId The ID of the epic to update.
     * @param request The CreateEpicRequest object containing updated epic details.
     * @return EpicResponse object representing the updated epic.
     */
    EpicResponse updateEpic(Long epicId, CreateEpicRequest request);

    /**
     * Deletes an epic by its ID.
     *
     * @param epicId The ID of the epic to delete.
     */
    void deleteEpic(Long epicId);

    /**
     * Reorders epics within a project based on a list of IDs.
     *
     * @param projectId The ID of the project.
     * @param epicIds List of Epic IDs in the desired order.
     */
    void reorderEpics(Long projectId, List<Long> epicIds);
}
