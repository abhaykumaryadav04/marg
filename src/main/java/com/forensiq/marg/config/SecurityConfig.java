package com.forensiq.marg.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import com.forensiq.marg.security.JwtAuthenticationConverter;




@Configuration
@EnableWebSecurity  
public class SecurityConfig {
    
     @Value("${jwt.secretkey}")
    private String jwtSecret;
    @Bean 
    public PasswordEncoder passwordEncoder(){
      return   new BCryptPasswordEncoder();
    }
    @Bean 
    public JwtDecoder jwtDecoder(){
        SecretKey key=new SecretKeySpec( jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key)
                           .macAlgorithm(MacAlgorithm.HS256)
                           .build();
    }
    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationConverter jwtAuthenticationConverter){
      return http.csrf(csrf->csrf.disable())
                  .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                  .authorizeHttpRequests(auth->auth.requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/test",
                                "/actuator/health"
                        ).permitAll()
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/api/provider/**")
                        .hasRole("PROVIDER")
                        .requestMatchers("/api/verifier/**")
                        .hasRole("VERIFIER")
                        .requestMatchers("/api/trainee/**")
                        .hasRole("TRAINEE")
                        .requestMatchers("/api/assessments/**")
                        .hasRole("TRAINEE")
                        .requestMatchers("/api/skills/**")
                        .hasRole("TRAINEE")
                        .requestMatchers("/api/skill-gaps/**")
                        .hasRole("TRAINEE")
                        .anyRequest()
                        .authenticated())
                        .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(
                                        jwtAuthenticationConverter
                                )
                        )).build();
    }
}
