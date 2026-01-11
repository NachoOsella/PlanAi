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

    @Override
    /**
     * Retrieves all epics for a specific project, ordered by their order index.
     *
     * @param projectId the ID of the project
     * @return a list of epic responses
     * @throws IllegalArgumentException if the project does not exist
     */
    public List<EpicResponse> getProjectEpics(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new IllegalArgumentException("Project with ID " + projectId + " does not exist.");
        }
        List<EpicEntity> epicsEntities = epicRepository.findByProjectIdOrderByOrderIndexAsc(projectId);
        return epicsEntities.stream().map(epicMapper::toResponse).toList();
    }

    @Override
    @Transactional
    /**
     * Creates a new epic for a specific project.
     *
     * @param projectId the ID of the project
     * @param request the request containing epic details
     * @return the created epic response
     * @throws IllegalArgumentException if the project does not exist
     */
    public EpicResponse createEpic(Long projectId, CreateEpicRequest request) {
        ProjectEntity project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project with ID " + projectId + " does not exist."));

        EpicEntity epicEntity = epicMapper.toEntity(request);
        epicEntity.setProject(project);
        epicEntity = epicRepository.save(epicEntity);
        return epicMapper.toResponse(epicEntity);
    }

    @Override
    @Transactional
    /**
     * Updates an existing epic.
     *
     * @param epicId the ID of the epic to update
     * @param request the request containing updated details
     * @return the updated epic response
     * @throws IllegalArgumentException if the epic does not exist
     */
    public EpicResponse updateEpic(Long epicId, CreateEpicRequest request) {
        EpicEntity epicEntity = epicRepository
                .findById(epicId)
                .orElseThrow(() -> new IllegalArgumentException("Epic with ID " + epicId + " does not exist."));

        epicMapper.updateEntityFromRequest(request, epicEntity);

        epicEntity = epicRepository.save(epicEntity);
        return epicMapper.toResponse(epicEntity);
    }

    @Override
    @Transactional
    /**
     * Deletes an epic by its ID.
     *
     * @param epicId the ID of the epic to delete
     * @throws IllegalArgumentException if the epic does not exist
     */
    public void deleteEpic(Long epicId) {
        if (!epicRepository.existsById(epicId)) {
            throw new IllegalArgumentException("Epic with ID " + epicId + " does not exist.");
        }
        epicRepository.deleteById(epicId);
    }

    @Override
    @Transactional
    /**
     * Reorders epics within a project based on a provided list of epic IDs. The order index of each epic is updated to
     * match its position in the list.
     *
     * @param projectId the ID of the project
     * @param epicIds the list of epic IDs in the desired order
     * @throws IllegalArgumentException if the project does not exist
     */
    public void reorderEpics(Long projectId, List<Long> epicIds) {
        if (!projectRepository.existsById(projectId)) {
            throw new IllegalArgumentException("Project with ID " + projectId + " does not exist.");
        }

        List<EpicEntity> epics = epicRepository.findByProjectIdOrderByOrderIndexAsc(projectId);
        Map<Long, EpicEntity> epicMap = epics.stream().collect(Collectors.toMap(EpicEntity::getId, epic -> epic));

        for (int i = 0; i < epicIds.size(); i++) {
            Long epicId = epicIds.get(i);
            EpicEntity epic = epicMap.get(epicId);
            if (epic != null) {
                epic.setOrderIndex(i);
            }
        }
        epicRepository.saveAll(epics);
    }
}
