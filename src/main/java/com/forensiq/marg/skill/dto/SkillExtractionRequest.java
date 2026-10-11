package com.forensiq.marg.skill.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SkillExtractionRequest(

        @NotBlank(message = "Text cannot be empty")
        @Size(max = 10000, message = "Text cannot exceed 10000 characters")
        String text
) {
} 