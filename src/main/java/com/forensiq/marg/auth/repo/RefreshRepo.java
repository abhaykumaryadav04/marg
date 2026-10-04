package com.forensiq.marg.auth.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forensiq.marg.auth.entity.RefreshToken;

public interface RefreshRepo extends JpaRepository<RefreshToken,UUID> {
Optional<RefreshToken> findByToken(String token);
void deleteAllByUserId(UUID userId);
}
