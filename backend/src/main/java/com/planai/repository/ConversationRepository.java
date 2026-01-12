package com.planai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.planai.model.entity.ConversationEntity;

/**
 * Repository interface for ConversationEntity operations.
 */
public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
    /**
     * Find all ConversationEntity records by project ID, ordered by createdAt in descending order.
     *
     * @param projectId The ID of the project.
     * @return List of ConversationEntity objects sorted by creation date descending.
     */
    List<ConversationEntity> findByProjectIdOrderByCreatedAtDesc(Long projectId);

    /**
     * Find a ConversationEntity by its ID, including its associated messages.
     *
     * @param id The ID of the conversation to find.
     * @return An Optional containing the ConversationEntity with messages if found, otherwise empty.
     */
    @EntityGraph(attributePaths = "messages")
    Optional<ConversationEntity> findById(Long id);
}
