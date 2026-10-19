package com.forensiq.marg.folowups.dto;


import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record FollowUpRequest(

        @NotBlank(message = "Training name is required")
        @Size(
                max = 200,
                message = "Training name cannot exceed 200 characters"
        )
        String trainingName,

        @NotNull(message = "Training completion date is required")
        @PastOrPresent(
                message = "Training completion date cannot be in the future"
        )
        LocalDateTime trainingCompletedAt,

        @NotNull(message = "Follow-up interval is required")
        @Min(value = 1, message = "Interval must be at least one day")
        @Max(value = 365, message = "Interval cannot exceed 365 days")
        Integer intervalDays
) {

}
