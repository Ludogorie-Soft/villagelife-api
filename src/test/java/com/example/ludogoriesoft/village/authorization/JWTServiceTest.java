package com.example.ludogorieSoft.village.authorization;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void testGetSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtService.getSecretKey());
        Key expectedSignInKey = Keys.hmacShaKeyFor(keyBytes);

        Key signInKey = jwtService.getSignInKey();

        assertEquals(expectedSignInKey, signInKey);
    }

//    @Test
//    void generateToken_ValidExtraClaimsAndUserDetails_ShouldReturnToken() {
//        String username = "user";
//        when(userDetails.getUsername()).thenReturn(username);
//
//        Map<String, Object> extraClaims = new HashMap<>();
//        extraClaims.put("user", "value1");
//
//
//        long currentTimeMillis = System.currentTimeMillis();
//        long expirationTimeMillis = currentTimeMillis + 1000 * 60 * 60 * 24;
//        Date expectedExpirationDate = new Date(expirationTimeMillis);
//
//        String expectedToken = Jwts.builder()
//                .setClaims(extraClaims)
//                .setSubject(username)
//                .setIssuedAt(new Date(currentTimeMillis))
//                .setExpiration(expectedExpirationDate)
//                .signWith(jwtService.getSignInKey(), SignatureAlgorithm.HS256)
//                .compact();
//
//        String generatedToken = jwtService.generateToken(extraClaims, userDetails);
//
//        assertEquals(expectedToken, generatedToken);
//    }

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
