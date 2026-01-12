package com.planai.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planai.model.dto.request.CreateStoryRequest;
import com.planai.model.dto.response.UserStoryResponse;
import com.planai.repository.EpicRepository;
import com.planai.repository.UserStoryRepository;
import com.planai.service.UserStoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link UserStoryService}.
 * Handles user story-related business logic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserStoryServiceImpl implements UserStoryService {

    private final UserStoryRepository userStoryRepository;
    private final EpicRepository epicRepository;

    /**
     * Retrieves all user stories associated with a specific epic.
     *
     * @param epicId The ID of the epic.
     * @return List of UserStoryResponse objects.
     */
    @Override
    public List<UserStoryResponse> getEpicStories(Long epicId) {
        return List.of();
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
        return null;
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
        return null;
    }

    /**
     * Deletes a user story by its ID.
     *
     * @param storyId The ID of the user story to delete.
     */
    @Override
    @Transactional
    public void deleteStory(Long storyId) {
    }
}
