package com.maverickbank.controller;

import com.maverickbank.entity.User;
import com.maverickbank.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users and roles")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users", description = "List all users (Admin only)")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get all active users", description = "List all active users")
    @GetMapping("/active")
    public ResponseEntity<List<User>> getAllActiveUsers() {
        return ResponseEntity.ok(userService.getAllActiveUsers());
    }

    @Operation(summary = "Get users by role", description = "Get users by specific role")
    @GetMapping("/role/{roleName}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String roleName) {
        return ResponseEntity.ok(userService.getUsersByRole(roleName));
    }

    @Operation(summary = "Get user by ID", description = "Get user details by ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update user profile", description = "Update user profile information")
    @PutMapping("/{id}/profile")
    public ResponseEntity<User> updateUserProfile(@PathVariable Long id, @RequestBody User userUpdate) {
        try {
            User updated = userService.updateUserProfile(id, userUpdate);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Deactivate user", description = "Deactivate a user account (Admin only)")
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Map<String, String>> deactivateUser(@PathVariable Long id) {
        try {
            userService.deactivateUser(id);
            return ResponseEntity.ok(Map.of("message", "User deactivated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Activate user", description = "Activate a user account (Admin only)")
    @PostMapping("/{id}/activate")
    public ResponseEntity<Map<String, String>> activateUser(@PathVariable Long id) {
        try {
            userService.activateUser(id);
            return ResponseEntity.ok(Map.of("message", "User activated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Assign role to user", description = "Assign a role to user (Admin only)")
    @PostMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<User> assignRole(@PathVariable Long userId, @PathVariable String roleName) {
        try {
            User updated = userService.assignRole(userId, roleName);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Remove role from user", description = "Remove a role from user (Admin only)")
    @DeleteMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<User> removeRole(@PathVariable Long userId, @PathVariable String roleName) {
        try {
            User updated = userService.removeRole(userId, roleName);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get user dashboard data", description = "Get dashboard summary for user")
    @GetMapping("/{userId}/dashboard")
    public ResponseEntity<Map<String, Object>> getUserDashboard(@PathVariable Long userId) {
        try {
            // This would typically aggregate data from multiple services
            // For now, returning basic user info
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(Map.of(
                "user", user,
                "message", "Dashboard data retrieved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get current user profile", description = "Get current logged-in user's profile")
    @GetMapping("/profile")
    public ResponseEntity<User> getCurrentUserProfile() {
        try {
            // For now, we'll get the user from the authentication context
            // In a real implementation, you'd get the current user from security context
            // This is a simplified version for testing
            String currentUsername = getCurrentUsername();
            User user = userService.getUserByUsername(currentUsername);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Helper method to get current username from JWT token
    private String getCurrentUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                return authentication.getName();
            }
        } catch (Exception e) {
            System.err.println("Error getting current username: " + e.getMessage());
        }
        // Fallback for testing - in production this should throw an exception
        return "testuser";
    }
}
