package com.planai.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planai.mapper.EpicMapper;
import com.planai.model.dto.request.CreateEpicRequest;
import com.planai.model.dto.response.EpicResponse;
import com.planai.model.entity.EpicEntity;
import com.planai.model.entity.ProjectEntity;
import com.planai.repository.EpicRepository;
import com.planai.repository.ProjectRepository;
import com.planai.service.EpicService;
import com.planai.exception.ResourceNotFoundException;
import com.planai.model.enums.PriorityEnum;
import com.planai.model.enums.StatusEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link EpicService}.
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class EpicServiceImpl implements EpicService {

    private final EpicRepository epicRepository;
    private final ProjectRepository projectRepository;
    private final EpicMapper epicMapper;

    public EpicServiceImpl(EpicRepository epicRepository, ProjectRepository projectRepository, EpicMapper epicMapper) {
        this.epicRepository = epicRepository;
        this.projectRepository = projectRepository;
        this.epicMapper = epicMapper;
    }

    /**
     * Retrieves all epics for a specific project, ordered by their order index.
     *
     * @param projectId the ID of the project
     * @return a list of {@link EpicResponse} representing the epics
     * @throws ResourceNotFoundException if the project does not exist
     */
    @Override
    public List<EpicResponse> getProjectEpics(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project", projectId);
        }
        List<EpicEntity> epicsEntities = epicRepository.findByProjectIdOrderByOrderIndexAsc(projectId);
        return epicsEntities.stream().map(epicMapper::toResponse).toList();
    }

    /**
     * Creates a new epic for a specific project.
     *
     * @param projectId the ID of the project
     * @param request the {@link CreateEpicRequest} containing epic details
     * @return the created {@link EpicResponse}
     * @throws ResourceNotFoundException if the project does not exist
     */
    @Override
    @Transactional
    public EpicResponse createEpic(Long projectId, CreateEpicRequest request) {
        ProjectEntity project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));

        EpicEntity epicEntity = epicMapper.toEntity(request);
        epicEntity.setProject(project);
        
        // Set defaults for required fields if not provided
        if (epicEntity.getStatus() == null) {
            epicEntity.setStatus(StatusEnum.TODO);
        }
        if (epicEntity.getPriority() == null) {
            epicEntity.setPriority(PriorityEnum.MEDIUM);
        }
        
        epicEntity = epicRepository.save(epicEntity);
        return epicMapper.toResponse(epicEntity);
    }

    /**
     * Updates an existing epic.
     *
     * @param epicId the ID of the epic to update
     * @param request the {@link CreateEpicRequest} containing updated details
     * @return the updated {@link EpicResponse}
     * @throws ResourceNotFoundException if the epic does not exist
     */
    @Override
    @Transactional
    public EpicResponse updateEpic(Long epicId, CreateEpicRequest request) {
        EpicEntity epicEntity = epicRepository
                .findById(epicId)
                .orElseThrow(() -> new ResourceNotFoundException("Epic", epicId));

        epicMapper.updateEntityFromRequest(request, epicEntity);

        epicEntity = epicRepository.save(epicEntity);
        return epicMapper.toResponse(epicEntity);
    }

    /**
     * Deletes an epic by its ID.
     *
     * @param epicId the ID of the epic to delete
     * @throws ResourceNotFoundException if the epic does not exist
     */
    @Override
    @Transactional
    public void deleteEpic(Long epicId) {
        if (!epicRepository.existsById(epicId)) {
            throw new ResourceNotFoundException("Epic", epicId);
        }
        epicRepository.deleteById(epicId);
    }

    /**
     * Reorders epics within a project based on a provided list of epic IDs. The order index of each epic is updated to
     * match its position in the list.
     *
     * @param projectId the ID of the project
     * @param epicIds the list of epic IDs in the desired order
     * @throws ResourceNotFoundException if the project does not exist
     * @throws IllegalArgumentException if the number of IDs doesn't match, if there are
     *             duplicate IDs, or if an ID doesn't belong to the project
     */
    @Override
    @Transactional
    public void reorderEpics(Long projectId, List<Long> epicIds) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project", projectId);
        }

        List<EpicEntity> epics = epicRepository.findByProjectIdOrderByOrderIndexAsc(projectId);

        if (epicIds.size() != epics.size()) {
            throw new IllegalArgumentException(
                    "Expected " + epics.size() + " epic IDs, but received " + epicIds.size());
        }

        if (epicIds.size() != epicIds.stream().distinct().count()) {
            throw new IllegalArgumentException("Duplicate epic IDs are not allowed.");
        }

        // Create map for O(1) lookup
        Map<Long, EpicEntity> epicMap = epics.stream().collect(Collectors.toMap(EpicEntity::getId, epic -> epic));

        // Validate all IDs belong to the project and update order
        for (int i = 0; i < epicIds.size(); i++) {
            Long epicId = epicIds.get(i);
            EpicEntity epic = epicMap.get(epicId);
            if (epic == null) {
                throw new IllegalArgumentException(
                        "Epic with ID " + epicId + " does not belong to project " + projectId);
            }
            epic.setOrderIndex(i);
        }

        epicRepository.saveAll(epics);
    }
}
