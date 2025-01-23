package com.example.ludogorieSoft.village.auth;

import com.example.ludogorieSoft.village.authorization.JWTService;
import com.example.ludogorieSoft.village.dtos.BusinessCardDTO;
import com.example.ludogorieSoft.village.dtos.VerificationTokenDTO;
import com.example.ludogorieSoft.village.dtos.request.AuthenticationRequest;
import com.example.ludogorieSoft.village.dtos.request.RegisterRequest;
import com.example.ludogorieSoft.village.dtos.request.ResetPasswordRequest;
import com.example.ludogorieSoft.village.dtos.request.VerificationRequest;
import com.example.ludogorieSoft.village.dtos.response.AuthenticationResponce;
import com.example.ludogorieSoft.village.enums.Role;
import com.example.ludogorieSoft.village.exeptions.*;
import com.example.ludogorieSoft.village.model.AlternativeUser;
import com.example.ludogorieSoft.village.model.VerificationToken;
import com.example.ludogorieSoft.village.repositories.AlternativeUserRepository;
import com.example.ludogorieSoft.village.repositories.BusinessCardRepository;
import com.example.ludogorieSoft.village.repositories.VerificationTokenRepository;
import com.example.ludogorieSoft.village.services.EmailSenderService;
import com.example.ludogorieSoft.village.services.ImageService;
import com.example.ludogorieSoft.village.services.VerificationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {
    @Mock
    private AlternativeUserRepository alternativeUserRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private VerificationTokenService verificationTokenService;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @Mock
    private EmailSenderService emailSenderService;
    @Mock
    private BusinessCardRepository businessCardRepository;
    @Mock
    private ImageService imageService;
    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerShouldRegisterAdminWhenAuthenticatedAsAdmin() {
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "username", "password", "1234567890", Role.ADMIN, null, null);
        Authentication authentication = mock(Authentication.class);
        AlternativeUser loggedUser = AlternativeUser.builder().username("adminUser").role(Role.ADMIN).build();
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("adminUser");
        when(alternativeUserRepository.findByUsername("adminUser")).thenReturn(loggedUser);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        String result = authenticationService.register(request);

        assertEquals("Administrator registered successfully!!!", result);
        verify(alternativeUserRepository).save(any(AlternativeUser.class));
    }

    @Test
    void registerShouldFailToRegisterAdminIfNotAuthenticatedAsAdmin() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("someNonAdminUser");

        AlternativeUser nonAdminUser = new AlternativeUser();
        nonAdminUser.setRole(Role.USER);
        when(alternativeUserRepository.findByUsername("someNonAdminUser")).thenReturn(nonAdminUser);

        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "username", "password", "1234567890", Role.ADMIN, null, null);

        Exception exception = assertThrows(AccessDeniedException.class, () -> authenticationService.register(request));

        assertEquals("You can not register new admin!!!", exception.getMessage());
    }

    @Test
    void registerShouldRegisterAgencyUserWithBusinessCard() {
        BusinessCardDTO businessCardDTO = new BusinessCardDTO(1L, "Company Name",null, null, "company@example.com", "1234567890", "Some Address", "http://website.com", 50, null);
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "username", "password", "1234567890", Role.AGENCY, "Job Title", businessCardDTO);

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(businessCardRepository.existsByEmail(businessCardDTO.getEmail())).thenReturn(false);

        VerificationTokenDTO verificationTokenDTO = mock(VerificationTokenDTO.class);
        when(verificationTokenDTO.getId()).thenReturn(1L);
        when(verificationTokenDTO.getToken()).thenReturn("mockToken123");
        when(verificationTokenService.createVerificationToken(any(AlternativeUser.class))).thenReturn(verificationTokenDTO);

        String result = authenticationService.register(request);

        assertEquals("Verification token send!!!", result);
        verify(alternativeUserRepository).save(any(AlternativeUser.class));
        verify(businessCardRepository, times(1)).existsByEmail(anyString());
        verify(emailSenderService).sendVerificationToken(eq(verificationTokenDTO), any(AlternativeUser.class));
        verify(verificationTokenRepository).save(any());
    }

    @Test
    void registerShouldThrowApiRequestExceptionForInvalidBusinessCardEmail() {
        BusinessCardDTO businessCardDTO = new BusinessCardDTO(1l, "Company Name", null, null, "invalid-email", "1234567890", "Some Address", "http://website.com", 50, null);
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "username", "password", "1234567890", Role.AGENCY, "Job Title", businessCardDTO);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> authenticationService.register(request));

        assertEquals("Invalid business card email!", exception.getMessage());
    }

    @Test
    void authenticate_ShouldReturnAuthenticationResponseWhenValid() {
        AuthenticationRequest request = new AuthenticationRequest("johndoe", "password");
        AlternativeUser authenticatedUser = mock(AlternativeUser.class);
        String jwtToken = "jwtToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(alternativeUserRepository.findByUsername(request.getUsername())).thenReturn(authenticatedUser);
        when(authenticatedUser.isEnabled()).thenReturn(true);
        when(jwtService.generateToken(authenticatedUser)).thenReturn(jwtToken);

        AuthenticationResponce response = authenticationService.authenticate(request);

        assertEquals(jwtToken, response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(authenticatedUser);
    }

    @Test
    void verifyVerificationToken_ShouldVerifyAccountSuccessfully() {
        VerificationRequest request = new VerificationRequest("token", "john@example.com");
        VerificationToken verificationToken = mock(VerificationToken.class);
        AlternativeUser user = mock(AlternativeUser.class);

        when(verificationTokenRepository.findByToken(request.getToken())).thenReturn(Optional.of(verificationToken));
        when(alternativeUserRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(verificationToken.getAlternativeUser()).thenReturn(user);
        when(user.isEnabled()).thenReturn(false);
        when(user.getEmail()).thenReturn(request.getEmail());
        when(verificationTokenService.isTokenExpired(verificationToken)).thenReturn(false);

        String result = authenticationService.verifyVerificationToken(request);

        assertEquals("Your account is verified!", result);
        verify(alternativeUserRepository).save(user);
        verify(verificationTokenRepository).delete(verificationToken);
    }

    @Test
    void verifyVerificationToken_ShouldThrowApiRequestExceptionForInvalidToken() {
        VerificationRequest request = new VerificationRequest("invalidToken", "john@example.com");

        when(verificationTokenRepository.findByToken(request.getToken())).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> authenticationService.verifyVerificationToken(request));
    }

    @Test
    void verifyVerificationToken_ShouldThrowTokenExpiredExceptionWhenTokenIsExpired() {
        VerificationRequest request = new VerificationRequest("token", "john@example.com");
        VerificationToken verificationToken = mock(VerificationToken.class);
        AlternativeUser user = mock(AlternativeUser.class);

        when(verificationTokenRepository.findByToken(request.getToken())).thenReturn(Optional.of(verificationToken));
        when(verificationToken.getAlternativeUser()).thenReturn(user);
        when(user.isEnabled()).thenReturn(false);
        when(verificationTokenService.isTokenExpired(verificationToken)).thenReturn(true);

        assertThrows(ApiRequestException.class, () -> authenticationService.verifyVerificationToken(request));
    }

    @Test
    void sendEmailToResetPassword_ShouldSendEmailSuccessfully() {
        String email = "user@example.com";
        AlternativeUser user = mock(AlternativeUser.class);
        when(alternativeUserRepository.findByEmail(email)).thenReturn(Optional.of(user));

        VerificationTokenDTO verificationTokenDTO = mock(VerificationTokenDTO.class);
        when(verificationTokenService.createVerificationToken(user)).thenReturn(verificationTokenDTO);
        when(verificationTokenDTO.getToken()).thenReturn("mockToken123");

        String result = authenticationService.sendEmailToResetPassword(email);

        assertEquals("Email sent successfully!", result);
        verify(verificationTokenRepository).save(any(VerificationToken.class));
        verify(emailSenderService).sendResetPasswordEmail(verificationTokenDTO, user);
    }

    @Test
    void sendEmailToResetPassword_ShouldThrowExceptionWhenUserNotFound() {
        String email = "user@example.com";
        when(alternativeUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> authenticationService.sendEmailToResetPassword(email));

        assertEquals("User not found!", exception.getMessage());
        verifyNoInteractions(verificationTokenService, verificationTokenRepository, emailSenderService);
    }

    @Test
    void resetPassword_ShouldThrowExceptionWhenTokenExpired() {
        Long userId = 1L;
        String token = "expiredToken";
        ResetPasswordRequest request = new ResetPasswordRequest(userId, token, "newPassword", "newPassword");

        VerificationToken verificationToken = mock(VerificationToken.class);
        when(verificationToken.getExpiryDate()).thenReturn(LocalDateTime.now().minusHours(1));

        AlternativeUser mockUser = mock(AlternativeUser.class);
        when(mockUser.getId()).thenReturn(userId);
        when(verificationToken.getAlternativeUser()).thenReturn(mockUser);

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));
        when(alternativeUserRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> authenticationService.resetPassword(request));

        assertEquals("Expired token!", exception.getMessage());
    }

    @Test
    void resetPassword_ShouldThrowExceptionWhenPasswordsDoNotMatch() {
        Long userId = 1L;
        String token = "validToken";
        ResetPasswordRequest request = new ResetPasswordRequest(userId, token, "newPassword", "differentPassword");
        AlternativeUser mockUser = mock(AlternativeUser.class);
        when(mockUser.getId()).thenReturn(userId);
        VerificationToken mockToken = mock(VerificationToken.class);
        when(mockToken.getAlternativeUser()).thenReturn(mockUser);
        LocalDateTime mockExpiryDate = LocalDateTime.now().plusDays(1);
        when(mockToken.getExpiryDate()).thenReturn(mockExpiryDate);
        when(alternativeUserRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(mockToken));
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> authenticationService.resetPassword(request));
        assertEquals("Passwords do not match", exception.getMessage());
    }
@Test
void resetPassword_ShouldChangePasswordSuccessfully() {
    Long userId = 1L;
    String newPassword = "newPassword123";
    String repeatedPassword = "newPassword123";
    String token = "validToken";
    AlternativeUser user = mock(AlternativeUser.class);
    VerificationToken verificationToken = mock(VerificationToken.class);

    ResetPasswordRequest request = new ResetPasswordRequest();
    request.setUserId(userId);
    request.setToken(token);
    request.setPassword(newPassword);
    request.setRepeatedPassword(repeatedPassword);

    when(alternativeUserRepository.findById(userId)).thenReturn(Optional.of(user));
    when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));
    when(verificationToken.getAlternativeUser()).thenReturn(user);
    when(verificationToken.getExpiryDate()).thenReturn(LocalDateTime.now().plusMinutes(10));
    when(user.getId()).thenReturn(userId);

    String result = authenticationService.resetPassword(request);

    assertEquals("Password changed successfully!", result);
    verify(alternativeUserRepository).save(user);
}

    @Test
    void resetPassword_ShouldThrowApiRequestException_WhenUserIdIsNull() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setUserId(null);
        request.setToken("validToken");
        request.setPassword("newPassword123");
        request.setRepeatedPassword("newPassword123");

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> authenticationService.resetPassword(request));

        assertEquals("User id can not be null!", exception.getMessage());
    }

    @Test
    void resetPassword_ShouldThrowApiRequestException_WhenTokenIsNull() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setUserId(1L);
        request.setToken(null);
        request.setPassword("newPassword123");
        request.setRepeatedPassword("newPassword123");

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> authenticationService.resetPassword(request));

        assertEquals("Token can not be null!", exception.getMessage());
    }

    @Test
    void resetPassword_ShouldThrowApiRequestException_WhenPasswordIsNull() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setUserId(1L);
        request.setToken("validToken");
        request.setPassword(null);
        request.setRepeatedPassword("newPassword123");

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> authenticationService.resetPassword(request));

        assertEquals("Password can not be null!", exception.getMessage());
    }

    @Test
    void resetPassword_ShouldThrowApiRequestException_WhenRepeatedPasswordIsNull() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setUserId(1L);
        request.setToken("validToken");
        request.setPassword("newPassword123");
        request.setRepeatedPassword(null);

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> authenticationService.resetPassword(request));

        assertEquals("Repeated password can not be null!", exception.getMessage());
    }

    @Test
    void resetPassword_ShouldThrowApiRequestException_WhenTokenIsInvalid() {
        ResetPasswordRequest request = new ResetPasswordRequest(1L, "invalidToken", "newPassword123", "newPassword123");
        AlternativeUser user = mock(AlternativeUser.class);
        when(alternativeUserRepository.findById(request.getUserId())).thenReturn(Optional.of(user));
        when(verificationTokenRepository.findByToken(request.getToken())).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> authenticationService.resetPassword(request));

        assertEquals("Invalid token!", exception.getMessage());
    }
}
