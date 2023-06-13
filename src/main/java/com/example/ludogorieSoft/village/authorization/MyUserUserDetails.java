package com.example.ludogoriesoft.village.authorization;//package com.ludogoriesoft.villagelifefrontend.authorization;
//
//import com.ludogoriesoft.villagelifefrontend.dtos.AdministratorRequest;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.List;
//@AllArgsConstructor
//@NoArgsConstructor
//public class MyUserUserDetails implements UserDetails {
//private AdministratorRequest administratorRequest;
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(administratorRequest.getRole().toString());
//        return List.of(authority);
//    }
//
//    @Override
//    public String getPassword() {
//        return administratorRequest.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return administratorRequest.getUsername();
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
