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
    public ResponseEntity<List<EpicResponse>> getProjectEpics(@PathVariable Long projectId) {
        return ResponseEntity.ok(epicService.getProjectEpics(projectId));
    }

    @PostMapping("/projects/{projectId}/epics")
    @Operation(summary = "Create a new epic in a project")
    public ResponseEntity<EpicResponse> createEpic(@PathVariable Long projectId,
            @Valid @RequestBody CreateEpicRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(epicService.createEpic(projectId, request));
    }

    @PutMapping("/epics/{id}")
    @Operation(summary = "Update an epic")
    public ResponseEntity<EpicResponse> updateEpic(@PathVariable Long id,
            @Valid @RequestBody CreateEpicRequest request) {
        return ResponseEntity.ok(epicService.updateEpic(id, request));
    }

    @DeleteMapping("/epics/{id}")
    @Operation(summary = "Delete an epic")
    public ResponseEntity<Void> deleteEpic(@PathVariable Long id) {
        epicService.deleteEpic(id);
        return ResponseEntity.noContent().build();
    }
}
