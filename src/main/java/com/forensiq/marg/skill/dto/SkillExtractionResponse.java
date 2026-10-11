package com.forensiq.marg.skill.dto;

import java.util.List;

public record SkillExtractionResponse(
             

    String text,
 
    List<SkillPrediction> skillprediction
) {
public record SkillPrediction(
      String skill,

            double confidence
) {
}
}
