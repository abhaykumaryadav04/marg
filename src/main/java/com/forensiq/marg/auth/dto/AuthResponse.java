package com.forensiq.marg.auth.dto;

import java.util.UUID;

public record AuthResponse(
    UUID userId,
        String email,
        String fullName,
        String role,
        String accessToken,
        String refreshToken,
        String message
) {

}
