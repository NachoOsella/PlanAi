package com.planai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planai.model.entity.EpicEntity;

/**
 * Repository interface for EpicEntity operations.
 */
public interface EpicRepository extends JpaRepository<EpicEntity, Long> {

    /**
     * Find all EpicEntity records by project ID, ordered by the 'order' field in ascending order.
     *
     * @param projectId The ID of the project.
     * @return List of EpicEntity objects sorted by order ascending.
     */
    List<EpicEntity> findByProjectIdOrderByOrderIndexAsc(Long projectId);
}
