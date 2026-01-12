package com.planai.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planai.mapper.TaskMapper;
import com.planai.model.dto.request.CreateTaskRequest;
import com.planai.model.dto.response.TaskResponse;
import com.planai.model.entity.TaskEntity;
import com.planai.model.entity.UserStoryEntity;
import com.planai.model.enums.StatusEnum;
import com.planai.repository.TaskRepository;
import com.planai.repository.UserStoryRepository;
import com.planai.service.TaskService;
import com.planai.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link TaskService}.
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserStoryRepository userStoryRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, UserStoryRepository userStoryRepository,
            TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userStoryRepository = userStoryRepository;
        this.taskMapper = taskMapper;
    }

    /**
     * Retrieves all tasks for a specific user story, ordered by their order index.
     *
     * @param storyId the ID of the user story
     * @return a list of {@link TaskResponse} representing the tasks
     * @throws ResourceNotFoundException if the user story does not exist
     */
    @Override
    public List<TaskResponse> getStoryTasks(Long storyId) {
        if (!userStoryRepository.existsById(storyId)) {
            throw new ResourceNotFoundException("UserStory", storyId);
        }
        List<TaskEntity> tasks = taskRepository.findByUserStoryIdOrderByOrderIndexAsc(storyId);
        return tasks.stream().map(taskMapper::toResponse).toList();
    }

    /**
     * Creates a new task within a user story.
     *
     * @param storyId the ID of the user story
     * @param request the {@link CreateTaskRequest} containing task details
     * @return the created {@link TaskResponse}
     * @throws ResourceNotFoundException if the user story does not exist
     */
    @Override
    @Transactional
    public TaskResponse createTask(Long storyId, CreateTaskRequest request) {
        UserStoryEntity userStory = userStoryRepository
                .findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("UserStory", storyId));
        TaskEntity taskEntity = taskMapper.toEntity(request);
        taskEntity.setUserStory(userStory);
        if (taskEntity.getStatus() == null) {
            taskEntity.setStatus(StatusEnum.TODO);
        }
        taskEntity = taskRepository.save(taskEntity);
        return taskMapper.toResponse(taskEntity);
    }

    /**
     * Updates an existing task's details.
     *
     * @param taskId the ID of the task to update
     * @param request the {@link CreateTaskRequest} containing updated task details
     * @return the updated {@link TaskResponse}
     * @throws ResourceNotFoundException if the task does not exist
     */
    @Override
    @Transactional
    public TaskResponse updateTask(Long taskId, CreateTaskRequest request) {
        TaskEntity taskEntity = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));
        taskMapper.updateEntityFromRequest(request, taskEntity);
        taskEntity = taskRepository.save(taskEntity);
        return taskMapper.toResponse(taskEntity);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param taskId the ID of the task to delete
     * @throws ResourceNotFoundException if the task does not exist
     */
    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        TaskEntity taskEntity = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));
        taskRepository.delete(taskEntity);
    }

    /**
     * Reorders tasks within a user story based on a provided list of task IDs. The order index of each task is updated
     * to match its position in the list.
     *
     * @param storyId the ID of the user story
     * @param taskIds the list of task IDs in the desired order
     * @throws ResourceNotFoundException if the user story does not exist
     * @throws IllegalArgumentException if the number of IDs doesn't match, if there
     *             are duplicate IDs, or if an ID doesn't belong to the user story
     */
    @Override
    @Transactional
    public void reorderTasks(Long storyId, List<Long> taskIds) {
        if (!userStoryRepository.existsById(storyId)) {
            throw new ResourceNotFoundException("UserStory", storyId);
        }

        List<TaskEntity> tasks = taskRepository.findByUserStoryIdOrderByOrderIndexAsc(storyId);

        if (taskIds.size() != tasks.size()) {
            throw new IllegalArgumentException(
                    "Expected " + tasks.size() + " task IDs, but received " + taskIds.size());
        }

        if (taskIds.size() != taskIds.stream().distinct().count()) {
            throw new IllegalArgumentException("Duplicate task IDs are not allowed.");
        }

        // Create map for O(1) lookup
        Map<Long, TaskEntity> taskMap = tasks.stream().collect(Collectors.toMap(TaskEntity::getId, task -> task));

        // Validate all IDs belong to the user story and update order
        for (int i = 0; i < taskIds.size(); i++) {
            Long taskId = taskIds.get(i);
            TaskEntity task = taskMap.get(taskId);
            if (task == null) {
                throw new IllegalArgumentException(
                        "Task with ID " + taskId + " does not belong to user story " + storyId);
            }
            task.setOrderIndex(i);
        }

        taskRepository.saveAll(tasks);
    }
}
