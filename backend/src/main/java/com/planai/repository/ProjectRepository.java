package com.planai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.planai.model.entity.ProjectEntity;

/**
 * Repository interface for ProjectEntity operations.
 */
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    /**
     * Find all ProjectEntity records ordered by createdAt in descending order.
     *
     * @return List of ProjectEntity objects sorted by creation date descending.
     */
    List<ProjectEntity> findAllByOrderByCreatedAtDesc();

    /**
     * Find a ProjectEntity by its ID, including its associated epics.
     *
     * @param id The ID of the project to find.
     * @return An Optional containing the ProjectEntity with its epics if found, otherwise empty.
     */
    @EntityGraph(attributePaths = "epics")
    Optional<ProjectEntity> findById(Long id);

}
