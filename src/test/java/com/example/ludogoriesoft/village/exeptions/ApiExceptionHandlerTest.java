package com.example.ludogorieSoft.village.exeptions;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;

 class ApiExceptionHandlerTest {

    private final ApiExceptionHandler apiExceptionHandler = new ApiExceptionHandler();

//    @Test
//    void handleDataIntegrityViolationException_shouldReturnConflictResponse() {
//        DataIntegrityViolationException ex = new DataIntegrityViolationException("Data integrity violation");
//
//        ResponseEntity<String> response = apiExceptionHandler.handleDataIntegrityViolationException(ex);
//
//        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
//        assertEquals("Duplicate entry error", response.getBody());
//    }
}
