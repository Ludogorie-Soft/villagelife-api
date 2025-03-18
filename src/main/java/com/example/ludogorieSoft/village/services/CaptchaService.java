package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.response.GoogleResponse;
import com.example.ludogorieSoft.village.exeptions.ReCaptchaInvalidException;
import com.example.ludogorieSoft.village.exeptions.ReCaptchaUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import java.net.URI;

@Service("captchaService")
public class CaptchaService extends AbstractCaptchaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaService.class);

    @Override
    public void processResponse(final String response) {
        securityCheck(response);

        final URI verifyUri = URI.create(String.format(RECAPTCHA_URL_TEMPLATE, getReCaptchaSecret(), response, getClientIP()));
        try {
            final GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
            LOGGER.debug("Google's response: {} ", googleResponse.toString());

            if (!googleResponse.isSuccess()) {
                if (googleResponse.hasClientError()) {
                    reCaptchaAttemptService.reCaptchaFailed(getClientIP());
                }
                throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
            }
        } catch (RestClientException rce) {
            throw new ReCaptchaUnavailableException("Registration unavailable at this time.  Please try again later.", rce);
        }
        reCaptchaAttemptService.reCaptchaSucceeded(getClientIP());
    }
}