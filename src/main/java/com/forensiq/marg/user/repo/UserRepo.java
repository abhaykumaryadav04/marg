package com.forensiq.marg.user.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forensiq.marg.user.entity.User;

public interface UserRepo extends JpaRepository<User,UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    
} 
