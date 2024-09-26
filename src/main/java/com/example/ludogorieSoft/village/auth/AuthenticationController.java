package com.example.ludogorieSoft.village.auth;

import com.example.ludogorieSoft.village.dtos.AlternativeUserDTO;
import com.example.ludogorieSoft.village.dtos.request.AuthenticationRequest;
import com.example.ludogorieSoft.village.dtos.request.RegisterRequest;
import com.example.ludogorieSoft.village.dtos.response.AuthenticationResponce;
import com.example.ludogorieSoft.village.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){
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
}
