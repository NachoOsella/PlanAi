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
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserStoryServiceImpl implements UserStoryService {

    private final UserStoryRepository userStoryRepository;
    private final EpicRepository epicRepository;

    @Override
    public List<UserStoryResponse> getEpicStories(Long epicId) {
        return List.of();
    }

    @Override
    @Transactional
    public UserStoryResponse createStory(Long epicId, CreateStoryRequest request) {
        return null;
    }

    @Override
    @Transactional
    public UserStoryResponse updateStory(Long storyId, CreateStoryRequest request) {
        return null;
    }

    @Override
    @Transactional
    public void deleteStory(Long storyId) {
    }
}
