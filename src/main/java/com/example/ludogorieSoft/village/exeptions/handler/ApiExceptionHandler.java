package com.example.ludogorieSoft.village.exeptions.handler;

import com.example.ludogorieSoft.village.exeptions.*;
import com.example.ludogorieSoft.village.slack.SlackMessage;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@AllArgsConstructor
@NoArgsConstructor
public class ApiExceptionHandler {
    private SlackMessage slackMessage;

    @ExceptionHandler(Exception.class)
    public void alertSlackChannelWhenUnhandledExceptionOccurs(Exception ex) {
        slackMessage.publishMessage("villagelife-notifications",
                "Error occured from the backend application ->" + ex.getMessage());
    }

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<String> handleApiRequestException(ApiRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(NoConsentException.class)
    public ResponseEntity<String> handleNoConsentException(NoConsentException ex) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ex.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Object> handleTokenExpiredException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token has expired");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> handleGlobalException(MalformedJwtException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
//        String errorMessage = ex.getCause().getCause().getMessage();
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
//    }

    @ExceptionHandler(UsernamePasswordException.class)
    public ResponseEntity<Object> handleUsernamePasswordException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong username or password");
    }
}
