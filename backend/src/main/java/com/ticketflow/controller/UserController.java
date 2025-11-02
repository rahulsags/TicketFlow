package com.ticketflow.controller;

import com.ticketflow.dto.UserResponse;
import com.ticketflow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/support-agents")
    public ResponseEntity<List<UserResponse>> getSupportAgents() {
        return ResponseEntity.ok(userService.getSupportAgents());
    }
}
