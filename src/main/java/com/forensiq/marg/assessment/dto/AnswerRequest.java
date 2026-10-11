package com.forensiq.marg.assessment.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnswerRequest(
       @NotNull
        UUID assessmentId,

        @NotBlank
        String questionId,

        @NotBlank
        String skill,

        @NotNull
        Boolean correct
) {

}
