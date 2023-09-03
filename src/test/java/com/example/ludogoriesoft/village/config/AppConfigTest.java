package com.example.ludogorieSoft.village.config;

import com.example.ludogorieSoft.village.authorization.JwtAuthenticationEntryPoint;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.security.core.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

 class AppConfigTest {

    private AppConfig appConfig;

    @BeforeEach
    void setUp() {
        appConfig = new AppConfig();
    }

    @Test
    void modelMapper_shouldReturnInstanceOfModelMapper() {
        ModelMapper modelMapper = appConfig.modelMapper();

        assertNotNull(modelMapper);
    }


    @Test
    void mappingJackson2HttpMessageConverter_shouldReturnInstanceOfMappingJackson2HttpMessageConverter() {
        AppConfig appConfig = new AppConfig();
        MappingJackson2HttpMessageConverter converter = appConfig.mappingJackson2HttpMessageConverter();

        assertNotNull(converter);
        converter.getObjectMapper();
        assertTrue(true);
        assertFalse(converter.getObjectMapper().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
    }
     @Test
     void testCommence() throws Exception {
         JwtAuthenticationEntryPoint authenticationEntryPoint = new JwtAuthenticationEntryPoint();
         MockHttpServletRequest request = new MockHttpServletRequest();
         MockHttpServletResponse response = new MockHttpServletResponse();
         AuthenticationException authException = new AuthenticationException("Unauthorized") {};

         authenticationEntryPoint.commence(request, response, authException);

         assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
         assertEquals("Unauthorized request!!!", response.getErrorMessage());
     }

}
