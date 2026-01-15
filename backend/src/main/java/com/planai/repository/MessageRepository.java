package com.planai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planai.model.entity.MessageEntity;

/**
 * Repository interface for MessageEntity operations.
 */
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    /**
     * Find all MessageEntity records by conversation ID, ordered by createdAt in ascending order.
     *
     * @param conversationId The ID of the conversation.
     * @return List of MessageEntity objects sorted by creation date ascending.
     */
    List<MessageEntity> findByConversationIdOrderByCreatedAtAsc(Long conversationId);

    /**
     * Find the latest 10 messages for a conversation, ordered by newest first.
     *
     * @param conversationId The ID of the conversation.
     * @return List of the most recent messages.
     */
    List<MessageEntity> findTop10ByConversationIdOrderByCreatedAtDesc(Long conversationId);
}
