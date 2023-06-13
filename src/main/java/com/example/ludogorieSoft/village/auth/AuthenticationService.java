package com.example.ludogoriesoft.village.auth;

import com.example.ludogoriesoft.village.authorization.JWTService;
import com.example.ludogoriesoft.village.enums.Role;
import com.example.ludogoriesoft.village.model.Administrator;
import com.example.ludogoriesoft.village.repositories.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AdministratorRepository administratorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponce register(RegisterRequest request) {
        var user = Administrator.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();
        administratorRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponce.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponce authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = administratorRepository.findByUsername(request.getUsername());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponce.builder()
                .token(jwtToken)
                .build();
    }
}
