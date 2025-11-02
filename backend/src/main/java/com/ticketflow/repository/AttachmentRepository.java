package com.ticketflow.repository;

import com.ticketflow.model.Attachment;
import com.ticketflow.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByTicket(Ticket ticket);
    List<Attachment> findByTicketId(Long ticketId);
}
