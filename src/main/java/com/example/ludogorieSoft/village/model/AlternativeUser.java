package com.example.ludogorieSoft.village.model;

import com.example.ludogorieSoft.village.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "alternative_users")
public class AlternativeUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name cannot be empty!")
    @Length(min = 2, message = "Full name should be at least than 2 characters long!")
    @Column(nullable = false)
    private String fullName;

    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Please enter a valid email address!")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Username cannot be empty!")
    @Length(max = 10, message = "Username should be less than 10 characters long!")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Password cannot be empty!")
    @Length(min = 8, message = "Password should be at least 8 characters long!")
    @Column(nullable = false)
    private String password;

    @Length(min = 10, message = "Phone number should be at least 10 numbers long!")
    @Column(unique = true)
    private String mobile;

    @OneToMany(mappedBy = "alternativeUser", cascade = CascadeType.ALL)
    private transient List<VerificationToken> verificationTokens;

    @OneToOne
    @JoinColumn(name = "user_search_data_id")
    private transient UserSearchData userSearchData;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @OneToOne
    private transient BusinessCard businessCard;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime deletedAt;

    private static final  boolean ENABLED = true;
    @Enumerated(EnumType.STRING)
    private Role role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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
