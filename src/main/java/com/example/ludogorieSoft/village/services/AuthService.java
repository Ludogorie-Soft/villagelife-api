package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.authorization.JWTService;
import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AdministratorService administratorService;
    private final HttpServletRequest request;
    private final JWTService jwtService;

    public AdministratorDTO getAdministratorInfo(){
        String authHeather = request.getHeader("Authorization");
        String jwt;
        String username;
        jwt = authHeather.substring(7);
        username = jwtService.extractUsername(jwt);
        return administratorService.findAdminByUsername(username);
    }
}
