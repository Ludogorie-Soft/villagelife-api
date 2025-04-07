package com.example.ludogorieSoft.village.services.interfaces;

import com.example.ludogorieSoft.village.exeptions.ReCaptchaInvalidException;

public interface Captchable {

    default void processResponse(final String response) throws ReCaptchaInvalidException {}

    default void processResponse(final String response, String action) throws ReCaptchaInvalidException {}

    String getReCaptchaSite();

    String getReCaptchaSecret();
}
