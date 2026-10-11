package com.forensiq.marg.assessment.dto;

import java.util.UUID;

public record StartAssessmentResponse(
     UUID assessmentId,

        String questionId,

        String skill,

        String topic,

        String questionText,

        String optionA,

        String optionB,

        String optionC,

        String optionD
) {
}
