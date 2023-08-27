package com.example.ludogorieSoft.village.exeptions;

import lombok.Getter;

import javax.servlet.http.HttpServletResponse;

@Getter
public class NoConsentException extends RuntimeException{
    private static final int HTTP_STATUS_CODE = HttpServletResponse.SC_EXPECTATION_FAILED;
    public NoConsentException(String message) {
        super(message);
    }

}
