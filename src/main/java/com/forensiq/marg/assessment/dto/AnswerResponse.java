package com.forensiq.marg.assessment.dto;

import java.util.UUID;

public record AnswerResponse(
        UUID assessmentId,

        String questionId,

        boolean correct,

        String nextQuestionId,

        String nextSkill,

        String nextTopic,

        String nextQuestionText,

        String optionA,

        String optionB,

        String optionC,

        String optionD
) {

}
