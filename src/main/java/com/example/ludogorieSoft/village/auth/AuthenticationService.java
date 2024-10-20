package com.example.ludogorieSoft.village.auth;

import com.example.ludogorieSoft.village.authorization.JWTService;
import com.example.ludogorieSoft.village.dtos.BusinessCardDTO;
import com.example.ludogorieSoft.village.dtos.VerificationTokenDTO;
import com.example.ludogorieSoft.village.dtos.request.AuthenticationRequest;
import com.example.ludogorieSoft.village.dtos.request.RegisterRequest;
import com.example.ludogorieSoft.village.dtos.request.VerificationRequest;
import com.example.ludogorieSoft.village.dtos.response.AuthenticationResponce;
import com.example.ludogorieSoft.village.enums.Role;
import com.example.ludogorieSoft.village.exeptions.AccessDeniedException;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.exeptions.TokenExpiredException;
import com.example.ludogorieSoft.village.exeptions.UsernamePasswordException;
import com.example.ludogorieSoft.village.model.AlternativeUser;
import com.example.ludogorieSoft.village.model.BusinessCard;
import com.example.ludogorieSoft.village.model.VerificationToken;
import com.example.ludogorieSoft.village.repositories.AlternativeUserRepository;
import com.example.ludogorieSoft.village.repositories.BusinessCardRepository;
import com.example.ludogorieSoft.village.repositories.VerificationTokenRepository;
import com.example.ludogorieSoft.village.services.EmailSenderService;
import com.example.ludogorieSoft.village.services.VerificationTokenService;
import com.example.ludogorieSoft.village.utils.TimestampUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AlternativeUserRepository alternativeUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final VerificationTokenService verificationTokenService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailSenderService emailSenderService;
    private final BusinessCardRepository businessCardRepository;

    public String register(RegisterRequest request) {
        checkRegistrationValidations(request);
        var user = AlternativeUser.builder()
                .fullName(request.getFullName())
                .createdAt(TimestampUtils.getCurrentTimestamp())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        if (request.getRole().equals(Role.ADMIN)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AlternativeUser loggedUser = alternativeUserRepository.findByUsername(authentication.getName());
            if (loggedUser == null || !loggedUser.getRole().equals(Role.ADMIN))
                throw new AccessDeniedException("You can not register new admin!!!");
            alternativeUserRepository.save(user);
            return "Administrator registered successfully!!!";
        } else if (request.getRole().equals(Role.AGENCY) || request.getRole().equals(Role.BUILDER) || request.getRole().equals(Role.INVESTOR)) {
            user.setJobTitle(request.getJobTitle());
            BusinessCard businessCard = BusinessCard.builder()
                    .name(request.getBusinessCardDTO().getName())
                    .email(request.getBusinessCardDTO().getEmail())
                    .phoneNumber(request.getBusinessCardDTO().getPhoneNumber())
                    .address(request.getBusinessCardDTO().getAddress())
                    .websiteLink(request.getBusinessCardDTO().getWebsiteLink())
                    .numberOfEmployees(request.getBusinessCardDTO().getNumberOfEmployees())
                    .build();
            user.setBusinessCard(businessCard);
        }
        user.setEnabled(false);
        alternativeUserRepository.save(user);
        VerificationTokenDTO verificationTokenDTO = verificationTokenService.createVerificationToken(user);
        verificationTokenRepository.save(new VerificationToken(verificationTokenDTO.getId(), verificationTokenDTO.getToken(),
                verificationTokenDTO.getExpiryDate(), user));
        emailSenderService.sendVerificationToken(verificationTokenDTO, user);
        return "Verification token send!!!";
    }

    public AuthenticationResponce authenticate(AuthenticationRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            var user = alternativeUserRepository.findByUsername(request.getUsername());
            if (!user.isEnabled()) throw new UsernamePasswordException("User not verified!!!");
            var jwtToken = jwtService.generateToken(user);

            return AuthenticationResponce.builder()
                    .token(jwtToken)
                    .build();
        } catch (DisabledException e) {
            throw new DisabledException("User not verified!!!");
        } catch (Exception e) {
            throw new UsernamePasswordException("Wrong username or password");
        }
    }

    public String verifyVerificationToken(VerificationRequest request) {
        Optional<VerificationToken> optionalVerificationToken = verificationTokenRepository.findByToken(request.getToken());
        if (optionalVerificationToken.isEmpty()) throw new ApiRequestException("Invalid token!");
        VerificationToken verificationToken = optionalVerificationToken.get();
        AlternativeUser user = verificationToken.getAlternativeUser();
        if (user.isEnabled()) throw new ApiRequestException("Account already verified!");
        if (verificationTokenService.isTokenExpired(verificationToken))
            throw new TokenExpiredException("Expired token!");
        if (!user.getEmail().equals(request.getEmail()))
            throw new ApiRequestException("Invalid token!");
        user.setEnabled(true);
        alternativeUserRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
        return "Your account is verified!";
    }

    private void checkRegistrationValidations(RegisterRequest request) {
        if (request.getFullName().isBlank()) throw new ApiRequestException("Full name is required!");
        if (request.getFullName().length() < 2)
            throw new ApiRequestException("Full name must be at least 2 characters!");
        if (request.getEmail().isBlank()) throw new ApiRequestException("Email is required!");
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
            throw new ApiRequestException("Invalid email!");
        if (request.getUsername().isBlank()) throw new ApiRequestException("Username is required!");
        if (request.getUsername().length() > 10)
            throw new ApiRequestException("Username can not be more 10 characters!");
        if (request.getPassword().isBlank()) throw new ApiRequestException("Password is required!");
        if (request.getPassword().length() < 8)
            throw new ApiRequestException("Password must be at least 8 characters long!");
        if (request.getMobile().isBlank() || request.getMobile().length() < 10)
            throw new ApiRequestException("Phone number can not be less than 10 characters!");
        if (request.getRole().equals(Role.BUILDER) || request.getRole().equals(Role.INVESTOR) || request.getRole().equals(Role.AGENCY))
            checkRegistrationValidationsForBusinesses(request);
    }

    private void checkRegistrationValidationsForBusinesses(RegisterRequest request) {
        BusinessCardDTO card = request.getBusinessCardDTO();
        if (card.getName().isBlank()) throw new ApiRequestException("Business card name is required!");
        if (card.getEmail().isBlank()) throw new ApiRequestException("Business card email is required!");
        if (!card.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
            throw new ApiRequestException("Invalid business card email!");
        if (businessCardRepository.existsByEmail(card.getEmail())) throw new ApiRequestException("Email already used!");
        if (!card.getPhoneNumber().matches("\\+?[0-9. ()-]{7,25}"))
            throw new ApiRequestException("Invalid business card phone number!");
        if (card.getNumberOfEmployees() < 0)
            throw new ApiRequestException("The number of employees cannot be negative!");
    }
}
