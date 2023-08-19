package com.example.ludogorieSoft.village.auth;

import com.example.ludogorieSoft.village.authorization.JWTService;
import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.request.AuthenticationRequest;
import com.example.ludogorieSoft.village.dtos.request.RegisterRequest;
import com.example.ludogorieSoft.village.dtos.response.AuthenticationResponce;
import com.example.ludogorieSoft.village.services.AuthService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;
    private final AuthService authService;
  private final JWTService jwtService;
  private final UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponce> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponce> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }
    @GetMapping("/get-info")
    public ResponseEntity<AdministratorDTO> getAdministratorInfo(){
        AdministratorDTO administratorDTO = authService.getAdministratorInfo();
        return new ResponseEntity<>(administratorDTO, HttpStatus.OK);
    }
    @GetMapping("/check")
    public ResponseEntity<String> authorizeAdminToken(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token format");
        }

        String jwtToken = token.substring(7);
        try {
            String username = jwtService.extractUsername(jwtToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                return ResponseEntity.ok("Authorized");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
