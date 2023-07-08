package com.example.ludogorieSoft.village.authorization;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

class JWTAuthenticationFilterTest {
    @InjectMocks
    private JWTAuthenticationFilter filter;

    @Mock
    private JWTService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        filter = new JWTAuthenticationFilter(jwtService, userDetailsService);
    }

    @Test
    void testDoFilterInternalWithoutAuthorizationHeaderShouldContinueFilterChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithInvalidAuthorizationHeaderShouldContinueFilterChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_WithExpiredToken_ShouldContinueFilterChain() throws ServletException, IOException {
        String expiredToken = "ExpiredToken";
        String username = "testuser";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + expiredToken);
        when(jwtService.extractUsername(expiredToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(expiredToken, userDetails)).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

//    @Test
//    public void testDoFilterInternalWithUnauthenticatedUserShouldContinueFilterChain() throws ServletException, IOException {
//        String validToken = "ValidToken";
//        String username = "testuser";
//        UserDetails userDetails = mock(UserDetails.class);
//
//        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
//        when(jwtService.extractUsername(validToken)).thenReturn(username);
//        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
//        when(jwtService.isTokenValid(validToken, userDetails)).thenReturn(true);
//        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(null);
//
//        filter.doFilterInternal(request, response, filterChain);
//
//        verify(filterChain).doFilter(request, response);
//    }
}
