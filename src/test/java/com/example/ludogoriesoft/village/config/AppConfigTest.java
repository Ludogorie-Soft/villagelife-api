package com.example.ludogorieSoft.village.config;

import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

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

}
