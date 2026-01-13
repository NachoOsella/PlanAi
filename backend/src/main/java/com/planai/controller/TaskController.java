package com.planai.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planai.model.dto.request.CreateTaskRequest;
import com.planai.model.dto.response.TaskResponse;
import com.planai.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Tasks", description = "Task management endpoints")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/stories/{storyId}/tasks")
    @Operation(summary = "Get all tasks for a story")
    public ResponseEntity<List<TaskResponse>> getStoryTasks(@PathVariable Long storyId) {
        return ResponseEntity.ok(taskService.getStoryTasks(storyId));
    }

    @PostMapping("/stories/{storyId}/tasks")
    @Operation(summary = "Create a new task in a story")
    public ResponseEntity<TaskResponse> createTask(@PathVariable Long storyId,
            @Valid @RequestBody CreateTaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(storyId, request));
    }

    @PutMapping("/tasks/{id}")
    @Operation(summary = "Update a task")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id,
            @Valid @RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @DeleteMapping("/tasks/{id}")
    @Operation(summary = "Delete a task")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
