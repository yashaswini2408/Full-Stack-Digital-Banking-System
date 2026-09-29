package com.maverickbank.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException ex) {
            throw new ExpiredJwtException(ex.getHeader(), ex.getClaims(), "JWT token has expired");
        } catch (MalformedJwtException ex) {
            throw new MalformedJwtException("JWT token is malformed");
        } catch (SignatureException ex) {
            throw new SignatureException("JWT token signature is invalid");
        } catch (JwtException ex) {
            throw new JwtException("JWT token processing error");
        }
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        } catch (JwtException ex) {
            // Token is invalid - let the global exception handler deal with it
            throw ex;
        } catch (Exception ex) {
            // For any other exception, return false to be safe
            return false;
        }
    }

    /**
     * Safe token validation method that doesn't throw exceptions
     * Returns false if token is invalid for any reason
     */
    public boolean isTokenValid(String token, String username) {
        try {
            return validateToken(token, username);
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Safe method to check if token is expired without throwing exceptions
     */
    public boolean isTokenSafelyExpired(String token) {
        try {
            return isTokenExpired(token);
        } catch (Exception ex) {
            return true; // Consider invalid tokens as expired
        }
    }

    /**
     * Safe method to extract username without throwing exceptions
     */
    public String extractUsernameSafely(String token) {
        try {
            return extractUsername(token);
        } catch (Exception ex) {
            return null;
        }
    }
}
