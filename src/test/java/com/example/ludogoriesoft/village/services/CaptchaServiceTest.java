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

    private static final String CLIENT_IP = "127.0.0.1";

    private static final String RESPONSE = "validResponse";
    private static final String SECRET = "test-secret";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processResponse_WhenClientBlocked_ThrowsReCaptchaInvalidException() {
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn(CLIENT_IP);
        when(reCaptchaAttemptService.isBlocked(CLIENT_IP)).thenReturn(true);

        assertThrows(ReCaptchaInvalidException.class, () -> captchaService.processResponse("anyResponse"));
        verify(reCaptchaAttemptService).isBlocked(CLIENT_IP);
    }

    @Test
    void processResponse_WhenResponseInvalid_ThrowsReCaptchaInvalidException() {
        String invalidResponse = "invalid!@#";
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn(CLIENT_IP);
        when(reCaptchaAttemptService.isBlocked(CLIENT_IP)).thenReturn(false);

        assertThrows(ReCaptchaInvalidException.class, () -> captchaService.processResponse(invalidResponse));
    }

    @Test
    void processResponse_WhenGoogleResponseHasClientError_IncrementsAttempts() {
        URI expectedUri = URI.create(String.format(AbstractCaptchaService.RECAPTCHA_URL_TEMPLATE, SECRET, RESPONSE, CLIENT_IP));

        setupClientAndSecret(CLIENT_IP, SECRET);
        GoogleResponse googleResponse = mock(GoogleResponse.class);
        when(googleResponse.isSuccess()).thenReturn(false);
        when(googleResponse.hasClientError()).thenReturn(true);
        when(restTemplate.getForObject(expectedUri, GoogleResponse.class)).thenReturn(googleResponse);

        assertThrows(ReCaptchaInvalidException.class, () -> captchaService.processResponse(RESPONSE));
        verify(reCaptchaAttemptService).reCaptchaFailed(CLIENT_IP);
    }

    @Test
    void processResponse_WhenGoogleResponseNotSuccessNoClientError_ThrowsException() {
        URI expectedUri = URI.create(String.format(AbstractCaptchaService.RECAPTCHA_URL_TEMPLATE, SECRET, RESPONSE, CLIENT_IP));

        setupClientAndSecret(CLIENT_IP, SECRET);
        GoogleResponse googleResponse = mock(GoogleResponse.class);
        when(googleResponse.isSuccess()).thenReturn(false);
        when(googleResponse.hasClientError()).thenReturn(false);
        when(restTemplate.getForObject(expectedUri, GoogleResponse.class)).thenReturn(googleResponse);

        assertThrows(ReCaptchaInvalidException.class, () -> captchaService.processResponse(RESPONSE));
        verify(reCaptchaAttemptService, never()).reCaptchaFailed(CLIENT_IP);
    }

    @Test
    void processResponse_WhenGoogleResponseSuccess_ResetsAttempts() {
        URI expectedUri = URI.create(String.format(AbstractCaptchaService.RECAPTCHA_URL_TEMPLATE, SECRET, RESPONSE, CLIENT_IP));

        setupClientAndSecret(CLIENT_IP, SECRET);
        GoogleResponse googleResponse = mock(GoogleResponse.class);
        when(googleResponse.isSuccess()).thenReturn(true);
        when(restTemplate.getForObject(expectedUri, GoogleResponse.class)).thenReturn(googleResponse);

        assertDoesNotThrow(() -> captchaService.processResponse(RESPONSE));
        verify(reCaptchaAttemptService).reCaptchaSucceeded(CLIENT_IP);
    }

    @Test
    void processResponse_WhenRestClientException_ThrowsReCaptchaUnavailable() {
        URI expectedUri = URI.create(String.format(AbstractCaptchaService.RECAPTCHA_URL_TEMPLATE, SECRET, RESPONSE, CLIENT_IP));

        setupClientAndSecret(CLIENT_IP, SECRET);
        when(restTemplate.getForObject(expectedUri, GoogleResponse.class)).thenThrow(new RestClientException("Error"));

        assertThrows(ReCaptchaUnavailableException.class, () -> captchaService.processResponse(RESPONSE));
    }

    @Test
    void processResponse_ConstructsCorrectUri() {
        String clientIP = "192.168.1.1";
        URI expectedUri = URI.create(String.format(AbstractCaptchaService.RECAPTCHA_URL_TEMPLATE, SECRET, RESPONSE, clientIP));

        when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.1,10.0.0.1");
        when(request.getRemoteAddr()).thenReturn("10.0.0.1");
        when(reCaptchaAttemptService.isBlocked(clientIP)).thenReturn(false);
        when(captchaSettings.getSecret()).thenReturn(SECRET);

        GoogleResponse googleResponse = mock(GoogleResponse.class);
        when(googleResponse.isSuccess()).thenReturn(true);
        when(restTemplate.getForObject(expectedUri, GoogleResponse.class)).thenReturn(googleResponse);

        captchaService.processResponse(RESPONSE);

        verify(restTemplate).getForObject(expectedUri, GoogleResponse.class);
    }

    private void setupClientAndSecret(String clientIP, String secret) {
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn(clientIP);
        when(reCaptchaAttemptService.isBlocked(clientIP)).thenReturn(false);
        when(captchaSettings.getSecret()).thenReturn(secret);
    }
}