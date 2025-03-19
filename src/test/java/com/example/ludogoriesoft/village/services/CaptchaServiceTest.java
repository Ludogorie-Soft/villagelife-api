package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.config.CaptchaSettings;
import com.example.ludogorieSoft.village.dtos.response.GoogleResponse;
import com.example.ludogorieSoft.village.exeptions.ReCaptchaInvalidException;
import com.example.ludogorieSoft.village.exeptions.ReCaptchaUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import javax.servlet.http.HttpServletRequest;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CaptchaServiceTest {

    @Mock
    private ReCaptchaAttemptService reCaptchaAttemptService;

    @Mock
    private RestOperations restTemplate;

    @Mock
    private HttpServletRequest request;

    @Mock
    private CaptchaSettings captchaSettings;

    @InjectMocks
    private CaptchaService captchaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processResponse_WhenClientBlocked_ThrowsReCaptchaInvalidException() {
        String clientIP = "127.0.0.1";
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn(clientIP);
        when(reCaptchaAttemptService.isBlocked(clientIP)).thenReturn(true);

        assertThrows(ReCaptchaInvalidException.class, () -> captchaService.processResponse("anyResponse"));
        verify(reCaptchaAttemptService).isBlocked(clientIP);
    }

    @Test
    void processResponse_WhenResponseInvalid_ThrowsReCaptchaInvalidException() {
        String invalidResponse = "invalid!@#";
        String clientIP = "127.0.0.1";
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn(clientIP);
        when(reCaptchaAttemptService.isBlocked(clientIP)).thenReturn(false);

        assertThrows(ReCaptchaInvalidException.class, () -> captchaService.processResponse(invalidResponse));
    }

    @Test
    void processResponse_WhenGoogleResponseHasClientError_IncrementsAttempts() {
        String response = "validResponse";
        String clientIP = "127.0.0.1";
        String secret = "test-secret";
        URI expectedUri = URI.create(String.format(AbstractCaptchaService.RECAPTCHA_URL_TEMPLATE, secret, response, clientIP));

        setupClientAndSecret(clientIP, secret);
        GoogleResponse googleResponse = mock(GoogleResponse.class);
        when(googleResponse.isSuccess()).thenReturn(false);
        when(googleResponse.hasClientError()).thenReturn(true);
        when(restTemplate.getForObject(expectedUri, GoogleResponse.class)).thenReturn(googleResponse);

        assertThrows(ReCaptchaInvalidException.class, () -> captchaService.processResponse(response));
        verify(reCaptchaAttemptService).reCaptchaFailed(clientIP);
    }

    @Test
    void processResponse_WhenGoogleResponseNotSuccessNoClientError_ThrowsException() {
        String response = "validResponse";
        String clientIP = "127.0.0.1";
        String secret = "test-secret";
        URI expectedUri = URI.create(String.format(AbstractCaptchaService.RECAPTCHA_URL_TEMPLATE, secret, response, clientIP));

        setupClientAndSecret(clientIP, secret);
        GoogleResponse googleResponse = mock(GoogleResponse.class);
        when(googleResponse.isSuccess()).thenReturn(false);
        when(googleResponse.hasClientError()).thenReturn(false);
        when(restTemplate.getForObject(expectedUri, GoogleResponse.class)).thenReturn(googleResponse);

        assertThrows(ReCaptchaInvalidException.class, () -> captchaService.processResponse(response));
        verify(reCaptchaAttemptService, never()).reCaptchaFailed(clientIP);
    }

    @Test
    void processResponse_WhenGoogleResponseSuccess_ResetsAttempts() {
        String response = "validResponse";
        String clientIP = "127.0.0.1";
        String secret = "test-secret";
        URI expectedUri = URI.create(String.format(AbstractCaptchaService.RECAPTCHA_URL_TEMPLATE, secret, response, clientIP));

        setupClientAndSecret(clientIP, secret);
        GoogleResponse googleResponse = mock(GoogleResponse.class);
        when(googleResponse.isSuccess()).thenReturn(true);
        when(restTemplate.getForObject(expectedUri, GoogleResponse.class)).thenReturn(googleResponse);

        assertDoesNotThrow(() -> captchaService.processResponse(response));
        verify(reCaptchaAttemptService).reCaptchaSucceeded(clientIP);
    }

    @Test
    void processResponse_WhenRestClientException_ThrowsReCaptchaUnavailable() {
        String response = "validResponse";
        String clientIP = "127.0.0.1";
        String secret = "test-secret";
        URI expectedUri = URI.create(String.format(AbstractCaptchaService.RECAPTCHA_URL_TEMPLATE, secret, response, clientIP));

        setupClientAndSecret(clientIP, secret);
        when(restTemplate.getForObject(expectedUri, GoogleResponse.class)).thenThrow(new RestClientException("Error"));

        assertThrows(ReCaptchaUnavailableException.class, () -> captchaService.processResponse(response));
    }

    @Test
    void processResponse_ConstructsCorrectUri() {
        String response = "testResponse";
        String secret = "testSecret";
        String clientIP = "192.168.1.1";
        URI expectedUri = URI.create(String.format(AbstractCaptchaService.RECAPTCHA_URL_TEMPLATE, secret, response, clientIP));

        when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.1,10.0.0.1");
        when(request.getRemoteAddr()).thenReturn("10.0.0.1");
        when(reCaptchaAttemptService.isBlocked(clientIP)).thenReturn(false);
        when(captchaSettings.getSecret()).thenReturn(secret);

        GoogleResponse googleResponse = mock(GoogleResponse.class);
        when(googleResponse.isSuccess()).thenReturn(true);
        when(restTemplate.getForObject(expectedUri, GoogleResponse.class)).thenReturn(googleResponse);

        captchaService.processResponse(response);

        verify(restTemplate).getForObject(expectedUri, GoogleResponse.class);
    }

    private void setupClientAndSecret(String clientIP, String secret) {
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn(clientIP);
        when(reCaptchaAttemptService.isBlocked(clientIP)).thenReturn(false);
        when(captchaSettings.getSecret()).thenReturn(secret);
    }
}