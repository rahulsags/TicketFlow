package com.ticketflow.repository;

import com.ticketflow.model.Comment;
import com.ticketflow.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTicketOrderByCreatedAtAsc(Ticket ticket);
    List<Comment> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
