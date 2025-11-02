package com.ticketflow.service;

import com.ticketflow.model.Ticket;
import com.ticketflow.model.TicketStatus;
import com.ticketflow.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    public void sendTicketCreatedEmail(Ticket ticket) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(ticket.getCreatedBy().getEmail());
            message.setSubject("Ticket Created - #" + ticket.getId());
            message.setText(String.format(
                    "Hello %s,\n\n" +
                    "Your ticket has been created successfully.\n\n" +
                    "Ticket #%d\n" +
                    "Subject: %s\n" +
                    "Priority: %s\n" +
                    "Status: %s\n\n" +
                    "We will review your ticket and get back to you soon.\n\n" +
                    "Best regards,\n" +
                    "TicketFlow Support Team",
                    ticket.getCreatedBy().getFullName(),
                    ticket.getId(),
                    ticket.getSubject(),
                    ticket.getPriority(),
                    ticket.getStatus()
            ));
            
            mailSender.send(message);
            log.info("Ticket created email sent to: {}", ticket.getCreatedBy().getEmail());
        } catch (Exception e) {
            log.error("Failed to send ticket created email", e);
        }
    }
    
    public void sendTicketAssignedEmail(Ticket ticket, User assignee) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(assignee.getEmail());
            message.setSubject("Ticket Assigned - #" + ticket.getId());
            message.setText(String.format(
                    "Hello %s,\n\n" +
                    "A new ticket has been assigned to you.\n\n" +
                    "Ticket #%d\n" +
                    "Subject: %s\n" +
                    "Priority: %s\n" +
                    "Status: %s\n" +
                    "Created by: %s\n\n" +
                    "Please review and respond to the ticket.\n\n" +
                    "Best regards,\n" +
                    "TicketFlow Support Team",
                    assignee.getFullName(),
                    ticket.getId(),
                    ticket.getSubject(),
                    ticket.getPriority(),
                    ticket.getStatus(),
                    ticket.getCreatedBy().getFullName()
            ));
            
            mailSender.send(message);
            log.info("Ticket assigned email sent to: {}", assignee.getEmail());
        } catch (Exception e) {
            log.error("Failed to send ticket assigned email", e);
        }
    }
    
    public void sendTicketStatusChangedEmail(Ticket ticket, TicketStatus oldStatus, TicketStatus newStatus) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(ticket.getCreatedBy().getEmail());
            message.setSubject("Ticket Status Updated - #" + ticket.getId());
            message.setText(String.format(
                    "Hello %s,\n\n" +
                    "The status of your ticket has been updated.\n\n" +
                    "Ticket #%d\n" +
                    "Subject: %s\n" +
                    "Previous Status: %s\n" +
                    "New Status: %s\n\n" +
                    "Thank you for using TicketFlow.\n\n" +
                    "Best regards,\n" +
                    "TicketFlow Support Team",
                    ticket.getCreatedBy().getFullName(),
                    ticket.getId(),
                    ticket.getSubject(),
                    oldStatus,
                    newStatus
            ));
            
            mailSender.send(message);
            log.info("Ticket status changed email sent to: {}", ticket.getCreatedBy().getEmail());
        } catch (Exception e) {
            log.error("Failed to send ticket status changed email", e);
        }
    }
}
