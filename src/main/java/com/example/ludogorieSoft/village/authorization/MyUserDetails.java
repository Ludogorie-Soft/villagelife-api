package com.example.ludogoriesoft.village.authorization;

import com.example.ludogoriesoft.village.dtos.AdministratorRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record MyUserDetails(AdministratorRequest administratorRequest) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(administratorRequest.getUsername());
        return List.of(authority);
    }

    @Override
    public String getPassword() {
        return administratorRequest.getPassword();
    }

    @Override
    public String getUsername() {
        return administratorRequest.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
