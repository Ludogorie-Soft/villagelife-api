package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VerificationTokenDTO;
import com.example.ludogorieSoft.village.model.AlternativeUser;
import com.example.ludogorieSoft.village.model.VerificationToken;
import com.example.ludogorieSoft.village.repositories.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@AllArgsConstructor
public class VerificationTokenService {
    private final ModelMapper modelMapper;
    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenDTO verificationTokenToVerificationTokenDTO(VerificationToken token) {
        return modelMapper.map(token, VerificationTokenDTO.class);
    }

    public VerificationToken verificationTokenDTOToVerificationToken(VerificationTokenDTO tokenDTO) {
        return modelMapper.map(tokenDTO, VerificationToken.class);
    }

    public VerificationTokenDTO createVerificationToken(AlternativeUser alternativeUser) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setExpiryDate(LocalDateTime.now().plus(15, ChronoUnit.MINUTES));
        verificationToken.setAlternativeUser(alternativeUser);
        verificationToken.setToken(generateToken());
        verificationToken.setAlternativeUser(alternativeUser);
        return verificationTokenToVerificationTokenDTO(verificationToken);
    }

    private String generateToken() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int length = 8;
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        return code.toString();
    }

    public boolean isTokenExpired(VerificationToken token) {
        return token.getExpiryDate().isBefore(LocalDateTime.now());
    }

    public String saveVerificationToken(VerificationTokenDTO token) {
        verificationTokenRepository.save(verificationTokenDTOToVerificationToken(token));
        return "Token saved successfully!";
    }
}
