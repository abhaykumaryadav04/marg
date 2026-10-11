package com.forensiq.marg.assessment.dto;

import java.util.List;
import java.util.UUID;

public record CompetencyResponse(

      UUID assessmentId,

        String status,

        List<SkillCompetency> competencies
) {

    public record SkillCompetency(

            String skill,

            double score

    ) {
    }
}
