package com.smartspec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartspec.model.entity.EpicEntity;

public interface EpicRepository extends JpaRepository<EpicEntity, Long> {

    /**
     * Find all EpicEntity records by project ID, ordered by the 'order' field in ascending order.
     * 
     * @param projectId The ID of the project.
     * @return List of EpicEntity objects sorted by order ascending.
     */
    List<EpicEntity> findByProjectIdOrderByOrderAsc(Long projectId);
}
