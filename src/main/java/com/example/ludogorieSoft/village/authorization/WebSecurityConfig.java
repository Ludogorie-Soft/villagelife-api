package com.example.ludogoriesoft.village.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImplementation();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers( "/villages").permitAll()
                .antMatchers( "/shop/home","/shop/products","shop/employees","/shop/orders/**","/shop/editProduct/**").hasAuthority("true")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/admins/login")
                .usernameParameter("username")
                .defaultSuccessUrl("/admins/menu", true)
                .permitAll()

                .and()
                .logout().permitAll()
                .logoutSuccessUrl("/admins/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");


//        http.authorizeRequests()
//                .antMatchers( "/assets/**","/css/**","/js/**","/pictures/**","/shop","/shop/customerLogin","/shop/employeeLogin","/shop/register",
//                        "/shop/all","/cart/**","/order/**","/shop/accountDetails","/","/shop/updateAddress","/shop/customerOrders","/shop/out").permitAll()
//                .antMatchers( "/shop/home","/shop/products","shop/employees","/shop/orders/**","/shop/editProduct/**").hasAuthority("EMPLOYEE")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().loginPage("/shop/employeeLogin")
//                .usernameParameter("id")
//                .defaultSuccessUrl("/shop/home", true)
//                .permitAll()
//
//                .and()
//                .logout().permitAll()
//                .logoutSuccessUrl("/shop")
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID");
//        http.authorizeRequests()
//                .antMatchers("/menu", "/picture/**", "/login", "/register_form", "/save_user").permitAll()
//                .antMatchers( "/all_resorts", "/all_hotels").hasAnyAuthority("USER","ADMIN")
//                .antMatchers("/**").hasAnyAuthority("ADMIN")
//                //.anyRequest().authenticated()
//                .and()
//                .formLogin().permitAll()
//                .defaultSuccessUrl("/menu", true)
//
//                .and()
//                .logout().permitAll()
//                .logoutSuccessUrl("/menu")
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID");

    }
}
