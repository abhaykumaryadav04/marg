package com.forensiq.marg.assessment.dto;

import java.util.List;

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
