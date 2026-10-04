package com.forensiq.marg.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank 
@NotNull 
String email,
@NotBlank 
 @Size(min = 8, max = 100)
String password,
@NotBlank 
String name
) {



}
