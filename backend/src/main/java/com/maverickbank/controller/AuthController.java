package com.maverickbank.controller;

import com.maverickbank.entity.User;
import com.maverickbank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Register new customer", description = "Register a new customer account")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User saved = userService.registerCustomer(user);
            return ResponseEntity.ok(Map.of("message", "User registered successfully", "userId", saved.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Register new employee", description = "Register a new bank employee (Admin only)")
    @PostMapping("/register-employee")
    public ResponseEntity<?> registerEmployee(@RequestBody User user) {
        try {
            User saved = userService.registerEmployee(user);
            return ResponseEntity.ok(Map.of("message", "Employee registered successfully", "userId", saved.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Login is handled by JwtAuthenticationFilter, so this is just for Swagger documentation
    @Operation(summary = "Login to get JWT token", description = "Provide username and password to receive a JWT token.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // This will never be called, but needed for Swagger UI
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Change password", description = "Change user password")
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            userService.changePassword(request.getUserId(), request.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }

    public static class ChangePasswordRequest {
        private Long userId;
        private String currentPassword;
        private String newPassword;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}
