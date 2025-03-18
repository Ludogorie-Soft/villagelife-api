package com.example.ludogorieSoft.village.exeptions;

public class ReCaptchaInvalidException extends ApiRequestException {
    public ReCaptchaInvalidException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ReCaptchaInvalidException(String message) {
        super(message);
    }
}
