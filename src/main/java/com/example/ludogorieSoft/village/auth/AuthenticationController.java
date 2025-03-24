package com.example.ludogorieSoft.village.auth;

import com.example.ludogorieSoft.village.dtos.AlternativeUserDTO;
import com.example.ludogorieSoft.village.dtos.request.AuthenticationRequest;
import com.example.ludogorieSoft.village.dtos.request.RegisterRequest;
import com.example.ludogorieSoft.village.dtos.request.ResetPasswordRequest;
import com.example.ludogorieSoft.village.dtos.request.VerificationRequest;
import com.example.ludogorieSoft.village.dtos.response.AuthenticationResponce;
import com.example.ludogorieSoft.village.services.AuthService;
import com.example.ludogorieSoft.village.services.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;
    private final AuthService authService;
    private final CaptchaService captchaService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request){
        final String response = request.getCaptchaResponse();
        captchaService.processResponse(response);
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponce> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }
    @GetMapping("/get-info")
    public ResponseEntity<AlternativeUserDTO> getAdministratorInfo(){
        AlternativeUserDTO alternativeUserDTO = authService.getAdministratorInfo();
        return new ResponseEntity<>(alternativeUserDTO, HttpStatus.OK);
    }
    @GetMapping("/check")
    public ResponseEntity<String> authorizeAdminToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok("Authorized");
    }
    @PostMapping("/verify-verification-token")
    public ResponseEntity<String> verifyVerificationToken(@RequestBody VerificationRequest verificationRequest) {
        return ResponseEntity.ok(service.verifyVerificationToken(verificationRequest));
    }

    @PostMapping("/send-reset-password-email")
    public ResponseEntity<String> resetPassword(@RequestParam("email") String email) {
        return ResponseEntity.ok(service.sendEmailToResetPassword(email));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(service.resetPassword(request));
    }
}
