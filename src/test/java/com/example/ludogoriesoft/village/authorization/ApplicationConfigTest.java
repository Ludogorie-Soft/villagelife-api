package com.example.ludogorieSoft.village.authorization;

import com.example.ludogorieSoft.village.model.Administrator;
import com.example.ludogorieSoft.village.repositories.AdministratorRepository;
import com.example.ludogorieSoft.village.services.AdministratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

 class ApplicationConfigTest {

    @Mock
    private AdministratorService administratorService;
    @Mock
    private AdministratorRepository administratorRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void userDetailsService_shouldReturnUserDetailsService() {
        ApplicationConfig applicationConfig = new ApplicationConfig(administratorRepository);

        String username = "admin";
        Administrator admin = new Administrator();
        when(administratorRepository.findByUsername(username)).thenReturn(admin);

        UserDetailsService userDetailsService = applicationConfig.userDetailsService();

        assertNotNull(userDetailsService);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertNotNull(userDetails);
    }

    @Test
    void authenticationProvider_shouldReturnAuthenticationProvider() {
        ApplicationConfig applicationConfig = new ApplicationConfig(administratorRepository);
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        DaoAuthenticationProvider expectedAuthProvider = new DaoAuthenticationProvider();
        expectedAuthProvider.setUserDetailsService(userDetailsService);
        expectedAuthProvider.setPasswordEncoder(passwordEncoder);

        AuthenticationProvider authenticationProvider = applicationConfig.authenticationProvider();

        assertNotNull(authenticationProvider);

    }

    @Test
    void authenticationManagerTest() throws Exception {
        ApplicationConfig applicationConfig = new ApplicationConfig(administratorRepository);
        AuthenticationConfiguration authenticationConfiguration = mock(AuthenticationConfiguration.class);
        AuthenticationManager expectedAuthenticationManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(expectedAuthenticationManager);

        AuthenticationManager authenticationManager = applicationConfig.authenticationManager(authenticationConfiguration);

        assertNotNull(authenticationManager);
    }

    @Test
    void passwordEncoderTest() {
        ApplicationConfig applicationConfig = new ApplicationConfig(administratorRepository);

        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();

        assertNotNull(passwordEncoder);
    }
}
