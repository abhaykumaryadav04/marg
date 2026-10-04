package com.forensiq.marg.user.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.forensiq.marg.user.ennumeration.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Table (name = "Customer")
@Builder 
public class User {
@Id 
@GeneratedValue 
private UUID id;
@Column (nullable = false)
private String name;
@Column (nullable = false,unique = true)
private String email;
@Column (nullable = false)
private String password;
@Column (nullable = false)
private boolean enable;
@Enumerated (EnumType.STRING)
private Role role;
private LocalDateTime createdAt;
@PrePersist 
void  onCreate(){
    createdAt=LocalDateTime.now();
}
}
