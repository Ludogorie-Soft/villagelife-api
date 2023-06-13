package com.example.ludogoriesoft.village.authorization;//package com.ludogoriesoft.villagelifefrontend.authorization;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new UserDetailsServiceImplementation();
//    }
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        System.out.println("4");
//
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService());
//        authProvider.setPasswordEncoder(passwordEncoder());
//        System.out.println("5");
//
//        return authProvider;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.authorizeRequests()
//                .antMatchers( "/villages","/images/**","/admins/register","/admins/save").permitAll()
//                .antMatchers( "/admins/home").hasAuthority("ADMIN")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().loginPage("/admins/login")
////                .usernameParameter("username")
//                .defaultSuccessUrl("/admins/menu", true)
//                .permitAll()
//
//                .and()
//                .logout().permitAll()
//                .logoutSuccessUrl("/admins/login")
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID");
//
//
//    }
//}
