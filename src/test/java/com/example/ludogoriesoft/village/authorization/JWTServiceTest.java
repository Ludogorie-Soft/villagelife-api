package com.example.ludogorieSoft.village.authorization;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
class JWTServiceTest {
    @Mock
    private JWTService jwtService;
    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JWTService("Y9fX8iAVeahSE4RDhyC4jqUlxvJa0g+zz7u5tJXj6ZObDsKJLiUi4GROECghCfEd");
                userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");
    }


    @Test
    void testExtractUsername() {
        String token = generateToken();
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testIsTokenValid() {
        String token = generateToken();
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void testIsTokenExpired() {
        String token = generateToken();
        boolean isExpired = jwtService.isTokenExpired(token);
        assertFalse(isExpired);
    }

    @Test
    void testExtractExpiration() {
        String token = generateToken();
        Date expiration = jwtService.extractExpiration(token);
        assertNotNull(expiration);
    }

    private String generateToken() {
        return jwtService.generateToken(new HashMap<>(), userDetails);
    }
    @Test
    void testGetSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtService.getSecretKey());
        Key expectedSignInKey = Keys.hmacShaKeyFor(keyBytes);

        Key signInKey = jwtService.getSignInKey();

        assertEquals(expectedSignInKey, signInKey);
    }

    @Test
    void extractUsernameInvalidToken() {
        String token = "token";

        assertThrows(JwtException.class, () -> jwtService.extractUsername(token));
    }

    @Test
    void testIsTokenNotValidThrowException() {
        String token = "token";
        when(userDetails.getUsername()).thenReturn("user");

        assertThrows(JwtException.class, () -> jwtService.isTokenValid(token, userDetails));
    }


    @Test
    void extractClaimInvalidTokenShouldThrowJwtException() {
        String token = "token";
        Function<Claims, String> claimResolver = Claims::getSubject;

        assertThrows(JwtException.class, () -> jwtService.extractClaim(token, claimResolver));
    }

}
