package com.ticketflow.repository;

import com.ticketflow.model.Rating;
import com.ticketflow.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByTicket(Ticket ticket);
    Optional<Rating> findByTicketId(Long ticketId);
}
