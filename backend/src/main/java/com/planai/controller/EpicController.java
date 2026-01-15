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

import com.planai.model.dto.request.CreateEpicRequest;
import com.planai.model.dto.response.EpicResponse;
import com.planai.service.EpicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Epics", description = "Epic management endpoints")
public class EpicController {

    private final EpicService epicService;

    public EpicController(EpicService epicService) {
        this.epicService = epicService;
    }

    @GetMapping("/projects/{projectId}/epics")
    @Operation(summary = "Get all epics for a project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Epics retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<List<EpicResponse>> getProjectEpics(
            @Parameter(description = "Project ID") @PathVariable Long projectId) {
        return ResponseEntity.ok(epicService.getProjectEpics(projectId));
    }

    @PostMapping("/projects/{projectId}/epics")
    @Operation(summary = "Create a new epic in a project")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Epic created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid epic data"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<EpicResponse> createEpic(
            @Parameter(description = "Project ID") @PathVariable Long projectId,
            @Valid @RequestBody CreateEpicRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(epicService.createEpic(projectId, request));
    }

    @PutMapping("/epics/{id}")
    @Operation(summary = "Update an epic")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Epic updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid epic data"),
            @ApiResponse(responseCode = "404", description = "Epic not found")
    })
    public ResponseEntity<EpicResponse> updateEpic(
            @Parameter(description = "Epic ID") @PathVariable Long id,
            @Valid @RequestBody CreateEpicRequest request) {
        return ResponseEntity.ok(epicService.updateEpic(id, request));
    }

    @DeleteMapping("/epics/{id}")
    @Operation(summary = "Delete an epic")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Epic deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Epic not found")
    })
    public ResponseEntity<Void> deleteEpic(@Parameter(description = "Epic ID") @PathVariable Long id) {
        epicService.deleteEpic(id);
        return ResponseEntity.noContent().build();
    }
}
