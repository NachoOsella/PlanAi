package com.planai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planai.model.entity.UserStoryEntity;

/**
 * Repository interface for UserStoryEntity operations.
 */
public interface UserStoryRepository extends JpaRepository<UserStoryEntity, Long> {

    /**
     * Find all UserStoryEntity records by epic ID, ordered by the 'order' field in ascending order.
     *
     * @param epicId The ID of the epic.
     * @return List of UserStoryEntity objects sorted by order ascending.
     */
    List<UserStoryEntity> findByEpicIdOrderByOrderIndexAsc(Long epicId);
}
