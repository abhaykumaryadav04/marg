package com.forensiq.marg.folowups.dto;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FollowUpRequestResponse(
      @NotBlank(message = "Employment status is required")
        @Pattern(
                regexp = "STUDENT|EMPLOYED|UNEMPLOYED|SELF_EMPLOYED|OTHER",
                message = "Invalid employment status"
        )
        String employmentStatus,

        @Size(max = 150)
        String currentJobRole,

        @Pattern(
                regexp = "FULL_TIME|PART_TIME|CONTRACT|INTERNSHIP|OTHER",
                message = "Invalid employment type"
        )
        String employmentType,

        @PastOrPresent(
                message = "Employment start date cannot be in the future"
        )
        LocalDateTime employmentStartDate,

        @DecimalMin(
                value = "0.00",
                message = "Monthly salary cannot be negative"
        )
        @Digits(
                integer = 10,
                fraction = 2,
                message = "Invalid salary format"
        )
        BigDecimal monthlySalary,

        @Pattern(
                regexp = "[A-Z]{3}",
                message = "Currency must be a three-letter uppercase code"
        )
        String salaryCurrency,

        @Pattern(
                regexp = "HIGHLY_RELEVANT|SOMEWHAT_RELEVANT|NOT_RELEVANT|NOT_APPLICABLE",
                message = "Invalid job relevance value"
        )
        String jobRelevance,

        @Size(max = 30, message = "Maximum 30 skills are allowed")
        
                @NotBlank(message = "Skill name cannot be blank")
                @Size(max = 100, message = "Skill name is too long")
                
        Set<String> skillsApplied,

        @Size(max = 1000)
        String additionalSkillGaps,

        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating cannot exceed 5")
        Integer trainingHelpfulnessRating,

        @Size(max = 2000)
        String notes,

        @NotNull(message = "Analytics consent selection is required")
        Boolean consentToUseInAggregatedAnalytics

) {

    @AssertTrue(
            message = "Currency is required when monthly salary is provided"
    )
    public boolean isSalaryCurrencyPresentWhenSalaryIsProvided() {
        return monthlySalary == null
                || (salaryCurrency != null
                && !salaryCurrency.isBlank());
    }


}
