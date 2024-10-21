package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VerificationTokenDTO;
import com.example.ludogorieSoft.village.model.AlternativeUser;
import com.example.ludogorieSoft.village.model.VerificationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificationTokenServiceTest {

    @InjectMocks
    private VerificationTokenService verificationTokenService;

    @Mock
    private ModelMapper modelMapper;

    private VerificationToken verificationToken;
    private VerificationTokenDTO verificationTokenDTO;
    private AlternativeUser alternativeUser;

    @BeforeEach
    public void setUp() {
        alternativeUser = new AlternativeUser();
        verificationToken = new VerificationToken();
        verificationToken.setId(1L);
        verificationToken.setToken("ABCD1234");
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        verificationToken.setAlternativeUser(alternativeUser);

        verificationTokenDTO = new VerificationTokenDTO();
        verificationTokenDTO.setId(1L);
        verificationTokenDTO.setToken("ABCD1234");
        verificationTokenDTO.setExpiryDate(verificationToken.getExpiryDate());
        verificationTokenDTO.setAlternativeUserDTO(null);
    }

    @Test
    void testVerificationTokenToVerificationTokenDTO() {
        when(modelMapper.map(verificationToken, VerificationTokenDTO.class)).thenReturn(verificationTokenDTO);

        VerificationTokenDTO result = verificationTokenService.verificationTokenToVerificationTokenDTO(verificationToken);

        assertNotNull(result);
        assertEquals(verificationToken.getToken(), result.getToken());
        assertEquals(verificationToken.getExpiryDate(), result.getExpiryDate());
    }

    @Test
    void testVerificationTokenDTOToVerificationToken() {
        when(modelMapper.map(verificationTokenDTO, VerificationToken.class)).thenReturn(verificationToken);

        VerificationToken result = verificationTokenService.verificationTokenDTOToVerificationToken(verificationTokenDTO);

        assertNotNull(result);
        assertEquals(verificationTokenDTO.getToken(), result.getToken());
        assertEquals(verificationTokenDTO.getExpiryDate(), result.getExpiryDate());
    }

    @Test
    void testCreateVerificationToken() {
        VerificationToken verificationToken1 = new VerificationToken();
        verificationToken1.setToken("generatedToken");
        verificationToken1.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        verificationToken1.setAlternativeUser(alternativeUser);

        when(modelMapper.map(any(VerificationToken.class), eq(VerificationTokenDTO.class)))
                .thenReturn(verificationTokenDTO);

        VerificationTokenDTO result = verificationTokenService.createVerificationToken(alternativeUser);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertNotNull(result.getExpiryDate());
        assertTrue(result.getExpiryDate().isAfter(LocalDateTime.now()));
        assertEquals(verificationTokenDTO.getToken(), result.getToken());
    }

    @Test
    void testIsTokenExpired() {
        VerificationToken expiredToken = new VerificationToken();
        expiredToken.setExpiryDate(LocalDateTime.now().minusMinutes(1));

        boolean result = verificationTokenService.isTokenExpired(expiredToken);

        assertTrue(result);

        VerificationToken validToken = new VerificationToken();
        validToken.setExpiryDate(LocalDateTime.now().plusMinutes(1));

        boolean validResult = verificationTokenService.isTokenExpired(validToken);

        assertFalse(validResult);
    }
}