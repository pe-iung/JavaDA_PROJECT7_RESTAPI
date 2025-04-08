package com.nnk.springboot.configuration;

import com.nnk.springboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Clock;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    Clock clock(){
        return Clock.systemUTC();
    }

    private final UserRepository userRepository;

//    @Bean
//    public UserDetailsService CustomUserDetailsService() {
//        return (String username) -> userRepository.findByUsername(username)
//                .map(user -> new UserDetails() {
//                    @Override
//                    public Collection<? extends GrantedAuthority> getAuthorities() {
//                        return List.of(new SimpleGrantedAuthority(user.getRole()));
//                    }
//
//                    @Override
//                    public String getPassword() {
//                        return user.getPassword();
//                    }
//
//                    @Override
//                    public String getUsername() {
//                        return user.getUsername();
//                    }
//
//                    public int getId() {
//                        return user.getId();
//                    }
//                })
//                .orElseThrow();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/h2-console").permitAll()
                    .requestMatchers("/app/login").permitAll()
                    .requestMatchers("/signup", "/css/**", "/js/**", "/webjars/**").permitAll()
                    .requestMatchers("/user/add", "/user/validate").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated();
                })

                .formLogin(form -> form
                        .loginPage("/app/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/403.html"))


                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }
}


