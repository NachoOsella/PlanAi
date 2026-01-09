package com.planai.service.interfaces;

import java.util.List;

import com.planai.model.dto.request.CreateStoryRequest;
import com.planai.model.dto.response.UserStoryResponse;

/**
 * Interface for User Story Service defining story-related operations.
 */
public interface IUserStoryService {

    /**
     * Retrieves all user stories associated with a specific epic.
     *
     * @param epicId The ID of the epic.
     * @return List of UserStoryResponse objects.
     */
    List<UserStoryResponse> getEpicStories(Long epicId);

    /**
     * Creates a new user story within an epic.
     *
     * @param epicId The ID of the epic where the story will be created.
     * @param request The CreateStoryRequest object containing story creation details.
     * @return UserStoryResponse object representing the created user story.
     */
    UserStoryResponse createStory(Long epicId, CreateStoryRequest request);

    /**
     * Updates an existing user story's details.
     *
     * @param storyId The ID of the user story to update.
     * @param request The CreateStoryRequest object containing updated story details.
     * @return UserStoryResponse object representing the updated user story.
     */
    UserStoryResponse updateStory(Long storyId, CreateStoryRequest request);

    /**
     * Deletes a user story by its ID.
     *
     * @param storyId The ID of the user story to delete.
     */
    void deleteStory(Long storyId);
}
