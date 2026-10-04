package com.forensiq.marg.auth.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.forensiq.marg.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
@Table (name ="refreshtoken")
@Data 
public class RefreshToken {
    @Id 
    @GeneratedValue 
    private UUID id;

    @Column(nullable = false, unique = true, length = 500)
    private String token;
    
     @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean revoked;

}
