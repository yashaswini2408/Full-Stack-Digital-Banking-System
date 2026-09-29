package com.maverickbank.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private UserDetails testUserDetails;
    private String testSecret = "mySecretKeyForTesting123456789012345678901234567890";

    @BeforeEach
    void setUp() {
        // Set the secret key using reflection
        ReflectionTestUtils.setField(jwtUtil, "secret", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationInMs", 86400000L); // 24 hours in milliseconds

        // Setup test user details
        testUserDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                .build();
    }

    @Test
    void testGenerateToken_Success() {
        // When
        String token = jwtUtil.generateToken(testUserDetails.getUsername());

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.startsWith("eyJ")); // JWT tokens start with eyJ
    }

    @Test
    void testExtractUsername_Success() {
        // Given
        String token = jwtUtil.generateToken(testUserDetails.getUsername());

        // When
        String extractedUsername = jwtUtil.extractUsername(token);

        // Then
        assertEquals("testuser", extractedUsername);
    }

    @Test
    void testExtractExpiration_Success() {
        // Given
        String token = jwtUtil.generateToken(testUserDetails.getUsername());

        // When
        Date expiration = jwtUtil.extractExpiration(token);

        // Then
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date())); // Should be in the future
    }

    @Test
    void testIsTokenExpired_ValidToken() {
        // Given
        String token = jwtUtil.generateToken(testUserDetails.getUsername());

        // When
        boolean isExpired = jwtUtil.isTokenExpired(token);

        // Then
        assertFalse(isExpired);
    }

    @Test
    void testValidateToken_ValidToken() {
        // Given
        String token = jwtUtil.generateToken(testUserDetails.getUsername());

        // When
        boolean isValid = jwtUtil.validateToken(token, testUserDetails.getUsername());

        // Then
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidUsername() {
        // Given
        String token = jwtUtil.generateToken(testUserDetails.getUsername());
        UserDetails differentUser = User.builder()
                .username("differentuser")
                .password("password")
                .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                .build();

        // When
        boolean isValid = jwtUtil.validateToken(token, differentUser.getUsername());

        // Then
        assertFalse(isValid);
    }

    @Test
    void testValidateToken_NullToken() {
        // When & Then
        assertThrows(Exception.class, () -> {
            jwtUtil.validateToken(null, testUserDetails.getUsername());
        });
    }

    @Test
    void testValidateToken_EmptyToken() {
        // When & Then
        assertThrows(Exception.class, () -> {
            jwtUtil.validateToken("", testUserDetails.getUsername());
        });
    }

    @Test
    void testValidateToken_MalformedToken() {
        // Given
        String malformedToken = "invalid.jwt.token";

        // When & Then
        assertThrows(Exception.class, () -> {
            jwtUtil.validateToken(malformedToken, testUserDetails.getUsername());
        });
    }

    @Test
    void testExtractUsername_MalformedToken() {
        // Given
        String malformedToken = "invalid.jwt.token";

        // When & Then
        assertThrows(Exception.class, () -> {
            jwtUtil.extractUsername(malformedToken);
        });
    }

    @Test
    void testGenerateToken_NullUserDetails() {
        // When & Then - The method doesn't throw exception for null, just generates with null subject
        String token = jwtUtil.generateToken(null);
        assertNotNull(token);
    }

    @Test
    void testTokenExpiration_CustomTime() {
        // Given - Set a very short expiration time (1 second)
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationInMs", 1);
        String token = jwtUtil.generateToken(testUserDetails.getUsername());

        // When - Wait for token to expire
        try {
            Thread.sleep(1100); // Wait 1.1 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Then - Expect an exception when checking an expired token
        assertThrows(Exception.class, () -> {
            jwtUtil.isTokenExpired(token);
        });
    }

    @Test
    void testExtractAllClaims_ValidToken() {
        // Given
        String token = jwtUtil.generateToken(testUserDetails.getUsername());

        // When - Extract username using the token
        String username = jwtUtil.extractUsername(token);
        Date expiration = jwtUtil.extractExpiration(token);

        // Then
        assertEquals("testuser", username);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testConsistentTokenGeneration() throws InterruptedException {
        // Given
        String username = "testuser";
        String token1 = jwtUtil.generateToken(username);
        Thread.sleep(1000); // Long enough delay to ensure different timestamps 
        String token2 = jwtUtil.generateToken(username);

        // When
        String username1 = jwtUtil.extractUsername(token1);
        String username2 = jwtUtil.extractUsername(token2);

        // Then
        assertEquals(username1, username2);
        assertEquals(username, username1);
        assertEquals(username, username2);
        
        // Tokens should be different due to different issue times
        assertNotEquals(token1, token2);
    }
}