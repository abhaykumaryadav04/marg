package com.forensiq.marg.folowups.dto;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.forensiq.marg.folowups.enumeration.FollowUpsStatus;

public record FollowUpResponse(

      UUID id,
     String trainingName,
    LocalDateTime trainingCompletedAt,
     Integer intervalDays,
     LocalDateTime dueDate,
    FollowUpsStatus status,
     String employmentStatus,
     String currentJobRole,
    String employmentType,

        LocalDateTime employmentStartDate,

        BigDecimal monthlySalary,

        String salaryCurrency,

        String jobRelevance,

        Set<String> skillsApplied,

        String additionalSkillGaps,

        Integer trainingHelpfulnessRating,

        String notes,

        LocalDateTime respondedAt,

        LocalDateTime verifiedAt,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

    
 ) {
} 