package com.example.ludogorieSoft.village.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReCaptchaAttemptServiceTest {

    private ReCaptchaAttemptService reCaptchaAttemptService;
    private final String TEST_KEY = "127.0.0.1";
    private final String ANOTHER_KEY = "192.168.1.1";

    @BeforeEach
    void setUp() {
        reCaptchaAttemptService = new ReCaptchaAttemptService();
    }

    @Test
    void isBlockedInitiallyReturnsFalse() {
        assertFalse(reCaptchaAttemptService.isBlocked(TEST_KEY));
    }

    @Test
    void reCaptchaFailedIncrementsAttempts() {
        reCaptchaAttemptService.reCaptchaFailed(TEST_KEY);
        reCaptchaAttemptService.reCaptchaFailed(TEST_KEY);
        reCaptchaAttemptService.reCaptchaFailed(TEST_KEY);
        assertFalse(reCaptchaAttemptService.isBlocked(TEST_KEY));

        reCaptchaAttemptService.reCaptchaFailed(TEST_KEY);
        assertTrue(reCaptchaAttemptService.isBlocked(TEST_KEY));
    }

    @Test
    void isBlockedAfterMaxAttempts() {
        for (int i = 0; i < 4; i++) {
            reCaptchaAttemptService.reCaptchaFailed(TEST_KEY);
        }
        assertTrue(reCaptchaAttemptService.isBlocked(TEST_KEY));
    }

    @Test
    void reCaptchaSucceededResetsAttempts() {
        for (int i = 0; i < 4; i++) {
            reCaptchaAttemptService.reCaptchaFailed(TEST_KEY);
        }
        assertTrue(reCaptchaAttemptService.isBlocked(TEST_KEY));

        reCaptchaAttemptService.reCaptchaSucceeded(TEST_KEY);
        assertFalse(reCaptchaAttemptService.isBlocked(TEST_KEY));

        reCaptchaAttemptService.reCaptchaFailed(TEST_KEY);
        assertFalse(reCaptchaAttemptService.isBlocked(TEST_KEY));
    }

    @Test
    void differentKeysAreHandledIndependently() {
        for (int i = 0; i < 4; i++) {
            reCaptchaAttemptService.reCaptchaFailed(TEST_KEY);
        }
        assertTrue(reCaptchaAttemptService.isBlocked(TEST_KEY));

        reCaptchaAttemptService.reCaptchaFailed(ANOTHER_KEY);
        assertFalse(reCaptchaAttemptService.isBlocked(ANOTHER_KEY));
    }

    @Test
    void exceedingMaxAttemptsStillBlocks() {
        for (int i = 0; i < 6; i++) {
            reCaptchaAttemptService.reCaptchaFailed(TEST_KEY);
        }
        assertTrue(reCaptchaAttemptService.isBlocked(TEST_KEY));
    }

    @Test
    void succeedWithoutPriorAttempts() {
        reCaptchaAttemptService.reCaptchaSucceeded(TEST_KEY);
        assertFalse(reCaptchaAttemptService.isBlocked(TEST_KEY));
    }
}
