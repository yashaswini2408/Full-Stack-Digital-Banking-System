package com.maverickbank.security;

import com.maverickbank.entity.User;
import com.maverickbank.entity.Role;
import com.maverickbank.repository.UserRepository;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User testUser;
    private Role customerRole;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        // Setup roles
        customerRole = new Role();
        customerRole.setId(1L);
        customerRole.setName("CUSTOMER");

        adminRole = new Role();
        adminRole.setId(3L);
        adminRole.setName("ADMIN");

        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("$2a$10$encoded.password.hash");
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setIsActive(true);
        testUser.setRoles(Set.of(customerRole));
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Given
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Then
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("$2a$10$encoded.password.hash", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        
        // Check authorities
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CUSTOMER")));

        verify(userRepository).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Given
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(username);
        });

        assertEquals("User not found with username: nonexistentuser", exception.getMessage());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_InactiveUser() {
        // Given
        String username = "inactiveuser";
        testUser.setIsActive(false);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Then
        assertNotNull(userDetails);
        assertFalse(userDetails.isEnabled()); // User should be disabled
        verify(userRepository).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_MultipleRoles() {
        // Given
        String username = "adminuser";
        testUser.setRoles(Set.of(customerRole, adminRole));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Then
        assertNotNull(userDetails);
        assertEquals(2, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CUSTOMER")));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        
        verify(userRepository).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_UserWithNullRoles() {
        // Given
        String username = "userwithnoroles";
        testUser.setRoles(null);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Then
        assertNotNull(userDetails);
        assertEquals(0, userDetails.getAuthorities().size());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_UserWithEmptyRoles() {
        // Given
        String username = "userwithemptyroles";
        testUser.setRoles(Set.of());
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Then
        assertNotNull(userDetails);
        assertEquals(0, userDetails.getAuthorities().size());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_NullUsername() {
        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(null);
        });

        assertEquals("User not found with username: null", exception.getMessage());
        verify(userRepository).findByUsername(null);
    }

    @Test
    void testLoadUserByUsername_EmptyUsername() {
        // Given
        String username = "";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(username);
        });

        assertEquals("User not found with username: ", exception.getMessage());
        verify(userRepository).findByUsername(username);
    }
}
