package com.planai.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planai.exception.ResourceNotFoundException;
import com.planai.mapper.UserStoryMapper;
import com.planai.model.dto.request.CreateStoryRequest;
import com.planai.model.dto.response.UserStoryResponse;
import com.planai.model.entity.EpicEntity;
import com.planai.model.entity.UserStoryEntity;
import com.planai.repository.EpicRepository;
import com.planai.repository.UserStoryRepository;
import com.planai.service.UserStoryService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link UserStoryService}. Handles user story-related business logic.
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class UserStoryServiceImpl implements UserStoryService {

    private final UserStoryRepository userStoryRepository;
    private final EpicRepository epicRepository;
    private final UserStoryMapper userStoryMapper;

    public UserStoryServiceImpl(UserStoryRepository userStoryRepository, EpicRepository epicRepository,
            UserStoryMapper userStoryMapper) {
        this.userStoryRepository = userStoryRepository;
        this.epicRepository = epicRepository;
        this.userStoryMapper = userStoryMapper;
    }

    /**
     * Retrieves all user stories associated with a specific epic.
     *
     * @param epicId The ID of the epic.
     * @return List of UserStoryResponse objects.
     */
    @Override
    public List<UserStoryResponse> getEpicStories(Long epicId) {
        if (!epicRepository.existsById(epicId)) {
            throw new ResourceNotFoundException("Epic", epicId);
        }
        List<UserStoryEntity> stories = userStoryRepository.findByEpicIdOrderByOrderIndexAsc(epicId);
        return stories.stream().map(userStoryMapper::toResponse).toList();
    }

    /**
     * Creates a new user story within an epic.
     *
     * @param epicId The ID of the epic where the story will be created.
     * @param request The CreateStoryRequest containing story creation details.
     * @return UserStoryResponse representing the created user story.
     */
    @Override
    @Transactional
    public UserStoryResponse createStory(Long epicId, CreateStoryRequest request) {
        EpicEntity epicEntity =
                epicRepository.findById(epicId).orElseThrow(() -> new ResourceNotFoundException("Epic", epicId));
        UserStoryEntity storyEntity = userStoryMapper.toEntity(request);
        storyEntity.setEpic(epicEntity);
        UserStoryEntity savededEntity = userStoryRepository.save(storyEntity);
        return userStoryMapper.toResponse(savededEntity);
    }

    /**
     * Updates an existing user story's details.
     *
     * @param storyId The ID of the user story to update.
     * @param request The CreateStoryRequest containing updated story details.
     * @return UserStoryResponse representing the updated user story.
     */
    @Override
    @Transactional
    public UserStoryResponse updateStory(Long storyId, CreateStoryRequest request) {
        UserStoryEntity storyEntity = userStoryRepository
                .findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("UserStory", storyId));
        userStoryMapper.updateEntityFromRequest(request, storyEntity);
        UserStoryEntity updatedEntity = userStoryRepository.save(storyEntity);
        return userStoryMapper.toResponse(updatedEntity);
    }

    /**
     * Deletes a user story by its ID.
     *
     * @param storyId The ID of the user story to delete.
     */
    @Override
    @Transactional
    public void deleteStory(Long storyId) {
        if (!userStoryRepository.existsById(storyId)) {
            throw new ResourceNotFoundException("UserStory", storyId);
        }
        userStoryRepository.deleteById(storyId);
    }
}
