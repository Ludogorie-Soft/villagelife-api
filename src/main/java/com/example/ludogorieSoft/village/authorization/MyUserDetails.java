//package com.example.ludogoriesoft.village.authorization;
//
//import com.example.ludogoriesoft.village.model.Administrator;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.List;
//
//public record MyUserDetails(Administrator administrator) implements UserDetails {
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(administrator.getUsername());
//        return List.of(authority);
//    }
//
//    @Override
//    public String getPassword() {
//        return administrator.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return administrator.getUsername();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
