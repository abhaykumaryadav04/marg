package com.forensiq.marg.skill.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SkillGapAnalysisRequest(
      @NotBlank(message = "Target role is required")
        @Size(max = 100, message = "Target role cannot exceed 100 characters")
        String targetRole
) {

}
