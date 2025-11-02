package com.ticketflow.controller;

import com.ticketflow.dto.AttachmentResponse;
import com.ticketflow.model.Attachment;
import com.ticketflow.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    
    private final FileService fileService;
    
    @PostMapping("/upload")
    public ResponseEntity<AttachmentResponse> uploadFile(
            @RequestParam("ticketId") Long ticketId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(fileService.uploadFile(ticketId, file));
    }
    
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<AttachmentResponse>> getTicketAttachments(@PathVariable Long ticketId) {
        return ResponseEntity.ok(fileService.getTicketAttachments(ticketId));
    }
    
    @GetMapping("/download/{attachmentId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long attachmentId) throws MalformedURLException {
        Resource resource = fileService.downloadFile(attachmentId);
        Attachment attachment = fileService.getAttachment(attachmentId);
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(resource);
    }
}
