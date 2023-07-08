package com.example.ludogorieSoft.village.authorization;

import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;


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
    }


    @Test
    void extractUsernameInvalidToken() {
        String token = "token";

        assertThrows(JwtException.class, () -> jwtService.extractUsername(token));
    }


////public boolean isTokenValid(String token, UserDetails userDetails) {
////    final String username = extractUsername(token);
////    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
////}
//    @Test
//    void isTokenValid_ValidTokenAndMatchingUserDetails_ShouldReturnTrue() {
//        String username = "username";
//        String token = "token";
//        when(jwtService.extractUsername(token)).thenReturn(token);
//        when(userDetails.getUsername()).thenReturn(username);
//
//        boolean valid = jwtService.isTokenValid(token, userDetails);
//
//        assertTrue(valid);
//    }

    @Test
    void testIsTokenNotValidThrowException() {
        String token = "token";
        when(userDetails.getUsername()).thenReturn("user");

        assertThrows(JwtException.class, () -> jwtService.isTokenValid(token, userDetails));
    }


    @Test
    void extractClaim_InvalidToken_ShouldThrowJwtException() {
        String token = "token";
        Function<Claims, String> claimResolver = Claims::getSubject;

        assertThrows(JwtException.class, () -> jwtService.extractClaim(token, claimResolver));
    }

}
