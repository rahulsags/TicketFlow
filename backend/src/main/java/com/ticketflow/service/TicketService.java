package com.ticketflow.service;

import com.ticketflow.dto.*;
import com.ticketflow.model.*;
import com.ticketflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AttachmentRepository attachmentRepository;
    private final RatingRepository ratingRepository;
    private final EmailService emailService;
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
    
    @Transactional
    public TicketResponse createTicket(TicketRequest request) {
        User currentUser = getCurrentUser();
        
        Ticket ticket = Ticket.builder()
                .subject(request.getSubject())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(TicketStatus.OPEN)
                .createdBy(currentUser)
                .build();
        
        ticket = ticketRepository.save(ticket);
        
        // Send email notification
        emailService.sendTicketCreatedEmail(ticket);
        
        return mapToTicketResponse(ticket);
    }
    
    @Transactional(readOnly = true)
    public List<TicketResponse> getMyTickets() {
        User currentUser = getCurrentUser();
        List<Ticket> tickets = ticketRepository.findByCreatedByOrderByCreatedAtDesc(currentUser);
        return tickets.stream()
                .map(this::mapToTicketResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TicketResponse> getAssignedTickets() {
        User currentUser = getCurrentUser();
        List<Ticket> tickets = ticketRepository.findByAssignedToOrderByCreatedAtDesc(currentUser);
        return tickets.stream()
                .map(this::mapToTicketResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TicketResponse> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAllByOrderByCreatedAtDesc();
        return tickets.stream()
                .map(this::mapToTicketResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        User currentUser = getCurrentUser();
        
        // Check access rights
        if (!hasAccessToTicket(ticket, currentUser)) {
            throw new RuntimeException("You don't have permission to view this ticket");
        }
        
        return mapToTicketResponse(ticket);
    }
    
    @Transactional
    public TicketResponse updateTicketStatus(Long id, TicketStatus status) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        User currentUser = getCurrentUser();
        
        // Check permissions
        if (!canUpdateTicketStatus(ticket, currentUser)) {
            throw new RuntimeException("You don't have permission to update this ticket");
        }
        
        TicketStatus oldStatus = ticket.getStatus();
        ticket.setStatus(status);
        
        if (status == TicketStatus.RESOLVED) {
            ticket.setResolvedAt(LocalDateTime.now());
        } else if (status == TicketStatus.CLOSED) {
            ticket.setClosedAt(LocalDateTime.now());
        }
        
        ticket = ticketRepository.save(ticket);
        
        // Send email notification
        emailService.sendTicketStatusChangedEmail(ticket, oldStatus, status);
        
        return mapToTicketResponse(ticket);
    }
    
    @Transactional
    public TicketResponse assignTicket(Long id, Long assigneeId) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("Assignee not found"));
        
        User currentUser = getCurrentUser();
        
        // Check permissions
        if (!canAssignTicket(currentUser)) {
            throw new RuntimeException("You don't have permission to assign tickets");
        }
        
        ticket.setAssignedTo(assignee);
        ticket = ticketRepository.save(ticket);
        
        // Send email notification
        emailService.sendTicketAssignedEmail(ticket, assignee);
        
        return mapToTicketResponse(ticket);
    }
    
    @Transactional
    public CommentResponse addComment(Long ticketId, CommentRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        User currentUser = getCurrentUser();
        
        // Check access rights
        if (!hasAccessToTicket(ticket, currentUser)) {
            throw new RuntimeException("You don't have permission to comment on this ticket");
        }
        
        Comment comment = Comment.builder()
                .content(request.getContent())
                .ticket(ticket)
                .user(currentUser)
                .build();
        
        comment = commentRepository.save(comment);
        
        return mapToCommentResponse(comment);
    }
    
    @Transactional(readOnly = true)
    public List<CommentResponse> getTicketComments(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        User currentUser = getCurrentUser();
        
        // Check access rights
        if (!hasAccessToTicket(ticket, currentUser)) {
            throw new RuntimeException("You don't have permission to view comments on this ticket");
        }
        
        List<Comment> comments = commentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId);
        return comments.stream()
                .map(this::mapToCommentResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public RatingResponse rateTicket(Long ticketId, RatingRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        User currentUser = getCurrentUser();
        
        // Only ticket creator can rate
        if (!ticket.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Only the ticket creator can rate the resolution");
        }
        
        // Ticket must be resolved or closed
        if (ticket.getStatus() != TicketStatus.RESOLVED && ticket.getStatus() != TicketStatus.CLOSED) {
            throw new RuntimeException("Can only rate resolved or closed tickets");
        }
        
        // Check if already rated
        if (ratingRepository.findByTicketId(ticketId).isPresent()) {
            throw new RuntimeException("Ticket has already been rated");
        }
        
        Rating rating = Rating.builder()
                .ticket(ticket)
                .stars(request.getStars())
                .feedback(request.getFeedback())
                .ratedBy(currentUser)
                .build();
        
        rating = ratingRepository.save(rating);
        
        return mapToRatingResponse(rating);
    }
    
    @Transactional(readOnly = true)
    public List<TicketResponse> searchTickets(String keyword, TicketStatus status, Priority priority) {
        User currentUser = getCurrentUser();
        
        Specification<Ticket> spec = Specification.where(null);
        
        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("subject")), "%" + keyword.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%")
                    )
            );
        }
        
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        
        if (priority != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("priority"), priority));
        }
        
        // Filter by user role
        if (currentUser.getRole() == Role.USER) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("createdBy"), currentUser)
            );
        }
        
        List<Ticket> tickets = ticketRepository.findAll(spec);
        return tickets.stream()
                .map(this::mapToTicketResponse)
                .collect(Collectors.toList());
    }
    
    private boolean hasAccessToTicket(Ticket ticket, User user) {
        if (user.getRole() == Role.ADMIN) {
            return true;
        }
        if (user.getRole() == Role.SUPPORT_AGENT) {
            return true;
        }
        return ticket.getCreatedBy().getId().equals(user.getId());
    }
    
    private boolean canUpdateTicketStatus(Ticket ticket, User user) {
        if (user.getRole() == Role.ADMIN) {
            return true;
        }
        if (user.getRole() == Role.SUPPORT_AGENT) {
            return ticket.getAssignedTo() != null &&
                   ticket.getAssignedTo().getId().equals(user.getId());
        }
        return false;
    }
    
    private boolean canAssignTicket(User user) {
        return user.getRole() == Role.ADMIN || user.getRole() == Role.SUPPORT_AGENT;
    }
    
    private TicketResponse mapToTicketResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .subject(ticket.getSubject())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .createdBy(mapToUserResponse(ticket.getCreatedBy()))
                .assignedTo(ticket.getAssignedTo() != null ? mapToUserResponse(ticket.getAssignedTo()) : null)
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .resolvedAt(ticket.getResolvedAt())
                .closedAt(ticket.getClosedAt())
                .commentCount(ticket.getComments() != null ? ticket.getComments().size() : 0)
                .attachmentCount(ticket.getAttachments() != null ? ticket.getAttachments().size() : 0)
                .rating(ticket.getRating() != null ? mapToRatingResponse(ticket.getRating()) : null)
                .build();
    }
    
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }
    
    private CommentResponse mapToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(mapToUserResponse(comment.getUser()))
                .createdAt(comment.getCreatedAt())
                .build();
    }
    
    private RatingResponse mapToRatingResponse(Rating rating) {
        return RatingResponse.builder()
                .id(rating.getId())
                .stars(rating.getStars())
                .feedback(rating.getFeedback())
                .ratedBy(mapToUserResponse(rating.getRatedBy()))
                .createdAt(rating.getCreatedAt())
                .build();
    }
}
