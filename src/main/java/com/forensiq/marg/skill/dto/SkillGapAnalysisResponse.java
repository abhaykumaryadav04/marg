package com.forensiq.marg.skill.dto;

import java.util.List;
import java.util.UUID;

public record SkillGapAnalysisResponse(
     UUID assessmentId,

        String targetRole,

        List<SkillGap> gaps
) {
    public record SkillGap(
         String skill,

            double currentScore,

            double requiredScore,

            double gapScore,

            double importance,

            double impactWeight,

            double intelligentGapScore,

            String priority
    ){

    }

}
