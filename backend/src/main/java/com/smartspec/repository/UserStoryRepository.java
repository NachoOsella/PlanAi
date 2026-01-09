package com.smartspec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartspec.model.entity.UserStoryEntity;

public interface UserStoryRepository extends JpaRepository<UserStoryEntity, Long> {

    /**
     * Find all UserStoryEntity records by epic ID, ordered by the 'order' field in ascending order.
     * 
     * @param epicId The ID of the epic.
     * @return List of UserStoryEntity objects sorted by order ascending.
     */
    List<UserStoryEntity> findByEpicIdOrderByOrderAsc(Long epicId);
}
