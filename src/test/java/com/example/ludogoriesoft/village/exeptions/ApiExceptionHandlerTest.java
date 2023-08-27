package com.example.ludogorieSoft.village.exeptions;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;

 class ApiExceptionHandlerTest {

     @Test
     void testHandleNoConsentException() {
         ApiExceptionHandler exceptionHandler = new ApiExceptionHandler();

         NoConsentException ex = new NoConsentException("User has not given consent");

         ResponseEntity<String> response = exceptionHandler.handleNoConsentException(ex);

         assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
     }
     @Test
     void testHandleApiRequestException() {
         ApiExceptionHandler exceptionHandler = new ApiExceptionHandler();

         ApiRequestException ex = new ApiRequestException("Bad request");

         ResponseEntity<String> response = exceptionHandler.handleApiRequestException(ex);

         assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
     }
}
