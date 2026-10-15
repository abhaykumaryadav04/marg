package com.forensiq.marg.skill.dto;

import java.util.List;

public record CourseRecommendationResponse(
    
       String targetRole,
        List<String> basedOnSkills,
        List<Course> recommendations,
        String modelVersion,
        String scoringMethod,
        String note
) {
   public record Course(

            String courseTitle,
            String courseDescription,
            String courseUrl,
            double similarityScore
    ) {
    }
}
