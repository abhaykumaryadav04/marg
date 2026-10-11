package com.forensiq.marg.assessment.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AssessmentDetailsResponse(

        UUID assessmentId,

        String status,

        LocalDateTime startedAt,

        LocalDateTime completedAt,

        List<AnswerDetails> responses
) {
public record AnswerDetails(

            UUID responseId,

            String questionId,

            String skill,

            boolean correct

    ) {
    }
}
