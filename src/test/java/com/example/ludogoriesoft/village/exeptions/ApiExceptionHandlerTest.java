package com.example.ludogorieSoft.village.exeptions;

import com.example.ludogorieSoft.village.exeptions.handler.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
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
     @Test
     void testHandleTokenExpiredException() {
         ApiExceptionHandler exceptionHandler = new ApiExceptionHandler();
         TokenExpiredException ex = new TokenExpiredException("Token has expired");
         ResponseEntity<Object> response = exceptionHandler.handleTokenExpiredException();
         assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
         assertEquals("Token has expired", response.getBody());
     }

     @Test
     void testHandleAccessDeniedException() {
         ApiExceptionHandler exceptionHandler = new ApiExceptionHandler();
         AccessDeniedException ex = new AccessDeniedException("Access Denied");
         ResponseEntity<String> response = exceptionHandler.handleAccessDeniedException(ex);
         assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
         assertEquals("Access Denied", response.getBody());
     }
}
