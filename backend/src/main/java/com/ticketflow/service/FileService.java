package com.ticketflow.service;

import com.ticketflow.dto.AttachmentResponse;
import com.ticketflow.dto.UserResponse;
import com.ticketflow.model.Attachment;
import com.ticketflow.model.Role;
import com.ticketflow.model.Ticket;
import com.ticketflow.model.User;
import com.ticketflow.repository.AttachmentRepository;
import com.ticketflow.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    
    private final AttachmentRepository attachmentRepository;
    private final TicketRepository ticketRepository;
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
    
    @Transactional
    public AttachmentResponse uploadFile(Long ticketId, MultipartFile file) throws IOException {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        User currentUser = getCurrentUser();
        
        // Check if user has access to this ticket
        if (!hasAccessToTicket(ticket, currentUser)) {
            throw new RuntimeException("You don't have permission to upload files to this ticket");
        }
        
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String uniqueFilename = UUID.randomUUID().toString() + extension;
        
        // Save file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Save attachment record
        Attachment attachment = Attachment.builder()
                .fileName(originalFilename)
                .filePath(uniqueFilename)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .ticket(ticket)
                .uploadedBy(currentUser)
                .build();
        
        attachment = attachmentRepository.save(attachment);
        
        return mapToAttachmentResponse(attachment);
    }
    
    @Transactional(readOnly = true)
    public List<AttachmentResponse> getTicketAttachments(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        User currentUser = getCurrentUser();
        
        if (!hasAccessToTicket(ticket, currentUser)) {
            throw new RuntimeException("You don't have permission to view attachments for this ticket");
        }
        
        return attachmentRepository.findByTicketId(ticketId).stream()
                .map(this::mapToAttachmentResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Resource downloadFile(Long attachmentId) throws MalformedURLException {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));
        
        User currentUser = getCurrentUser();
        
        if (!hasAccessToTicket(attachment.getTicket(), currentUser)) {
            throw new RuntimeException("You don't have permission to download this file");
        }
        
        Path filePath = Paths.get(uploadDir).resolve(attachment.getFilePath()).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        
        if (!resource.exists()) {
            throw new RuntimeException("File not found");
        }
        
        return resource;
    }
    
    @Transactional(readOnly = true)
    public Attachment getAttachment(Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));
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
    
    private AttachmentResponse mapToAttachmentResponse(Attachment attachment) {
        return AttachmentResponse.builder()
                .id(attachment.getId())
                .fileName(attachment.getFileName())
                .fileType(attachment.getFileType())
                .fileSize(attachment.getFileSize())
                .uploadedBy(mapToUserResponse(attachment.getUploadedBy()))
                .uploadedAt(attachment.getUploadedAt())
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
}
