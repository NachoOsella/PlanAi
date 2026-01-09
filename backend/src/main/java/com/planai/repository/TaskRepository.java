package com.planai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planai.model.entity.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    /**
     * Find all TaskEntity records by user story ID, ordered by the 'order' field in ascending order.
     * 
     * @param userStoryId The ID of the user story.
     * @return List of TaskEntity objects sorted by order ascending.
     */
    List<TaskEntity> findByUserStoryIdOrderByOrderIndexAsc(Long userStoryId);
}
