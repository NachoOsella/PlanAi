package com.planai.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planai.model.dto.request.CreateTaskRequest;
import com.planai.model.dto.response.TaskResponse;
import com.planai.service.TaskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link TaskService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    @Override
    public List<TaskResponse> getStoryTasks(Long storyId) {
        return List.of();
    }

    @Override
    @Transactional
    public TaskResponse createTask(Long storyId, CreateTaskRequest request) {
        return null;
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long taskId, CreateTaskRequest request) {
        return null;
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        // Implementation
    }

    @Override
    @Transactional
    public void reorderTasks(Long storyId, List<Long> taskIds) {
        // Implementation
    }
}
