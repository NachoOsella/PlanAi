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

import com.planai.model.dto.request.CreateStoryRequest;
import com.planai.model.dto.response.UserStoryResponse;
import com.planai.service.UserStoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "User Stories", description = "User Story management endpoints")
public class UserStoryController {

    private final UserStoryService userStoryService;

    public UserStoryController(UserStoryService userStoryService) {
        this.userStoryService = userStoryService;
    }

    @GetMapping("/epics/{epicId}/stories")
    @Operation(summary = "Get all stories for an epic")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stories retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Epic not found")
    })
    public ResponseEntity<List<UserStoryResponse>> getEpicStories(
            @Parameter(description = "Epic ID") @PathVariable Long epicId) {
        return ResponseEntity.ok(userStoryService.getEpicStories(epicId));
    }

    @PostMapping("/epics/{epicId}/stories")
    @Operation(summary = "Create a new story in an epic")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Story created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid story data"),
            @ApiResponse(responseCode = "404", description = "Epic not found")
    })
    public ResponseEntity<UserStoryResponse> createStory(
            @Parameter(description = "Epic ID") @PathVariable Long epicId,
            @Valid @RequestBody CreateStoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userStoryService.createStory(epicId, request));
    }

    @PutMapping("/stories/{id}")
    @Operation(summary = "Update a story")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Story updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid story data"),
            @ApiResponse(responseCode = "404", description = "Story not found")
    })
    public ResponseEntity<UserStoryResponse> updateStory(
            @Parameter(description = "Story ID") @PathVariable Long id,
            @Valid @RequestBody CreateStoryRequest request) {
        return ResponseEntity.ok(userStoryService.updateStory(id, request));
    }

    @DeleteMapping("/stories/{id}")
    @Operation(summary = "Delete a story")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Story deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Story not found")
    })
    public ResponseEntity<Void> deleteStory(@Parameter(description = "Story ID") @PathVariable Long id) {
        userStoryService.deleteStory(id);
        return ResponseEntity.noContent().build();
    }
}
