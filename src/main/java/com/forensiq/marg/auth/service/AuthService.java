package com.forensiq.marg.auth.service;


import java.time.LocalDateTime;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.forensiq.marg.auth.dto.AuthResponse;
import com.forensiq.marg.auth.dto.LoginRequest;
import com.forensiq.marg.auth.dto.RegisterRequest;
import com.forensiq.marg.auth.entity.RefreshToken;
import com.forensiq.marg.auth.repo.RefreshRepo;
import com.forensiq.marg.security.JwtService;
import com.forensiq.marg.user.ennumeration.Role;
import com.forensiq.marg.user.entity.User;
import com.forensiq.marg.user.repo.UserRepo;




@Service 
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final RefreshRepo refreshRepo;
    public AuthService(PasswordEncoder passwordEncoder,UserRepo userRepo,JwtService jwtService,RefreshRepo refreshRepo){
        this.passwordEncoder=passwordEncoder;
        this.userRepo=userRepo;
        this.jwtService=jwtService;
        this.refreshRepo=refreshRepo;
    }
    public AuthResponse register(RegisterRequest request){
        if(userRepo.existsByEmail(request.email())){
            throw new RuntimeException("Email already register");
        }
        User user=User.builder().email(request.email())
                                 .name(request.name())
                                 .role(Role.TRAINEE)
                                 .password(passwordEncoder.encode(request.password()))
                                 .enable(true)
                                 .build();
        userRepo.save(user);
     
        return  new AuthResponse( 
             user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name(),
                null,null,
                "Registration successful"
);
    }
    public AuthResponse login(LoginRequest request){
        User user=userRepo.findByEmail(request.email()).orElseThrow(()-> new RuntimeException("First register"));
       System.out.println("Entered password: " +request.password());
    System.out.println("Stored hash: " + user.getPassword());
          if(!passwordEncoder.matches(request.password(),user.getPassword())){
            throw new RuntimeException("Enter correct password");
          }
          if (!user.isEnable()) { 
            throw new RuntimeException("User account is disabled");  
          }
            String refreshTokenValue =jwtService.generateRefreshToken(user);
            RefreshToken refreshToken=RefreshToken.builder()
                                                  .token(refreshTokenValue)
                                                  .user(user)
                                                  .expiresAt(LocalDateTime.now().plusDays(7))
                                                  .revoked(false)
                                                  .build();
            refreshRepo.save(refreshToken);
          String token=jwtService.generateToken(user);
             return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name(),
                token,refreshTokenValue,
                "Login successful");
    }
    public AuthResponse getCurrentUser(String userId) {
        User user = userRepo.findById(
            java.util.UUID.fromString(userId)
    ).orElseThrow(() ->
            new RuntimeException("User not found")
    );
   return new AuthResponse(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getRole().name(),
            null,null,
            "Current user"
    );
    }
    public AuthResponse refreshAccessToken(String refreshToken) {
       RefreshToken refreshToken2=refreshRepo.findByToken(refreshToken).orElseThrow(()->new RuntimeException("Invalid refresh token"));
       if(refreshToken2.isRevoked()){
        throw new RuntimeException("Refresh token has been revoke");
       }
       if(refreshToken2.getExpiresAt().isBefore(LocalDateTime.now())){
        throw new RuntimeException("Refresh token has expired");
       }
       User user=refreshToken2.getUser();
       String newrefresh=jwtService.generateToken(user);
         return new AuthResponse(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getRole().name(),
            newrefresh,
            refreshToken,
            "Access token refreshed"
    );

    }
    public void logout(String token) {
         RefreshToken refreshToken = refreshRepo.findByToken(token)
                    .orElseThrow(() -> new RuntimeException( "Invalid refresh token" ) );
            refreshToken.setRevoked(true);
            refreshRepo.save(refreshToken);
    }

  

}
