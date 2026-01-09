package com.planai.service;

import java.util.List;

import com.planai.model.dto.request.CreateTaskRequest;
import com.planai.model.dto.response.TaskResponse;

/**
 * Interface for Task Service defining task-related operations.
 */
public interface TaskService {

    /**
     * Retrieves all tasks associated with a specific user story.
     *
     * @param storyId The ID of the user story.
     * @return List of TaskResponse objects.
     */
    List<TaskResponse> getStoryTasks(Long storyId);

    /**
     * Creates a new task within a user story.
     *
     * @param storyId The ID of the user story where the task will be created.
     * @param request The CreateTaskRequest object containing task creation details.
     * @return TaskResponse object representing the created task.
     */
    TaskResponse createTask(Long storyId, CreateTaskRequest request);

    /**
     * Updates an existing task's details.
     *
     * @param taskId The ID of the task to update.
     * @param request The CreateTaskRequest object containing updated task details.
     * @return TaskResponse object representing the updated task.
     */
    TaskResponse updateTask(Long taskId, CreateTaskRequest request);

    /**
     * Deletes a task by its ID.
     *
     * @param taskId The ID of the task to delete.
     */
    void deleteTask(Long taskId);

    /**
     * Reorders tasks within a user story based on a list of IDs.
     *
     * @param storyId The ID of the user story.
     * @param taskIds List of Task IDs in the desired order.
     */
    void reorderTasks(Long storyId, List<Long> taskIds);
}
