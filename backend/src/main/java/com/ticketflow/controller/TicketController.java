package com.ticketflow.controller;

import com.ticketflow.dto.*;
import com.ticketflow.model.Priority;
import com.ticketflow.model.TicketStatus;
import com.ticketflow.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    
    private final TicketService ticketService;
    
    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.createTicket(request));
    }
    
    @GetMapping("/my-tickets")
    public ResponseEntity<List<TicketResponse>> getMyTickets() {
        return ResponseEntity.ok(ticketService.getMyTickets());
    }
    
    @GetMapping("/assigned")
    @PreAuthorize("hasAnyRole('SUPPORT_AGENT', 'ADMIN')")
    public ResponseEntity<List<TicketResponse>> getAssignedTickets() {
        return ResponseEntity.ok(ticketService.getAssignedTickets());
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('SUPPORT_AGENT', 'ADMIN')")
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPPORT_AGENT', 'ADMIN')")
    public ResponseEntity<TicketResponse> updateTicketStatus(
            @PathVariable Long id,
            @RequestParam TicketStatus status
    ) {
        return ResponseEntity.ok(ticketService.updateTicketStatus(id, status));
    }
    
    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('SUPPORT_AGENT', 'ADMIN')")
    public ResponseEntity<TicketResponse> assignTicket(
            @PathVariable Long id,
            @RequestParam Long assigneeId
    ) {
        return ResponseEntity.ok(ticketService.assignTicket(id, assigneeId));
    }
    
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request
    ) {
        return ResponseEntity.ok(ticketService.addComment(id, request));
    }
    
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getTicketComments(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketComments(id));
    }
    
    @PostMapping("/{id}/rate")
    public ResponseEntity<RatingResponse> rateTicket(
            @PathVariable Long id,
            @Valid @RequestBody RatingRequest request
    ) {
        return ResponseEntity.ok(ticketService.rateTicket(id, request));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<TicketResponse>> searchTickets(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) Priority priority
    ) {
        return ResponseEntity.ok(ticketService.searchTickets(keyword, status, priority));
    }
}
