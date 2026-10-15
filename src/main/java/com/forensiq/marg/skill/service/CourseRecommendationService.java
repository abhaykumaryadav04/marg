package com.forensiq.marg.skill.service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.forensiq.marg.skill.client.Model4Client;
import com.forensiq.marg.skill.dto.CourseRecommendationResponse;
import com.forensiq.marg.skill.dto.SkillGapAnalysisResponse;
import com.forensiq.marg.user.entity.TraineeProfile;
import com.forensiq.marg.user.repo.TraineeProfileRepo;

@Service 
public class CourseRecommendationService {
    private final Model4Client model4Client;
     private final TraineeProfileRepo traineeProfileRepo;

    private final SkillAnalysisService skillAnalysisService;

   

    public CourseRecommendationService(Model4Client model4Client,TraineeProfileRepo traineeProfileRepo,
    SkillAnalysisService skillAnalysisService
    ){
        this.model4Client=model4Client;
        this.traineeProfileRepo=traineeProfileRepo;
        this.skillAnalysisService=skillAnalysisService;
    }
    @Transactional (readOnly = true)
    public CourseRecommendationResponse courseRecommendationResponse(UUID userId,int topk){
        TraineeProfile traineeProfile=traineeProfileRepo.findByUserId(userId).orElseThrow(()-> new RuntimeException("User not found"));
        String targetRole = traineeProfile.getTargetJobRole();
          if (targetRole == null || targetRole.isBlank()) {

            throw new ResponseStatusException( HttpStatus.CONFLICT,
                    "Set your target job role in your trainee profile first" );
        }
        targetRole = targetRole.strip();
        SkillGapAnalysisResponse gapAnalysis =
                skillAnalysisService.getMyskillGaps(userId);

        List<SkillGapAnalysisResponse.SkillGap> gaps =
                gapAnalysis.gaps() == null
                        ? List.of()
                        : gapAnalysis.gaps();
    
    List<String> skillsToImprove = gaps.stream()
                .filter(Objects::nonNull)
                .filter(gap ->
                        gap.gapScore() > 0
                                || gap.intelligentGapScore() > 0
                )
                .sorted(
                        Comparator.comparingDouble(
                                SkillGapAnalysisResponse.SkillGap::intelligentGapScore
                        ).reversed()
                )
                .map(SkillGapAnalysisResponse.SkillGap::skill)
                .filter(Objects::nonNull)
                .map(String::strip)
                .filter(skill -> !skill.isBlank())
                .distinct()
                .limit(12)
                .toList();
                  String skillDescription;

        if (skillsToImprove.isEmpty()) {
            skillDescription =
                    "Relevant technical skills, competencies, tools "
                    + "and responsibilities for the target role.";

        } else {
            skillDescription =
                    "Skills to improve based on the latest competency "
                    + "assessment: "
                    + String.join(", ", skillsToImprove)
                    + ". Learn the tools, concepts and practical "
                    + "competencies required for this role.";
        }
        String jobDescription = "Target role: " + targetRole + ". "+ skillDescription;
     Model4Client.Model4Response modelResponse =
                model4Client.courseRecomendation(
                        targetRole,
                        jobDescription,
                        topk
                );
       List<CourseRecommendationResponse.Course> recommendations =
                modelResponse.recommendations()
                        .stream()
                        .map(course ->
                                new CourseRecommendationResponse.Course(
                                        course.courseTitle(),
                                        course.courseDescription(),
                                        course.courseUrl(),
                                        course.similarityScore()
                                )
                        )
                        .toList();
        
        return new CourseRecommendationResponse(
                targetRole,
                skillsToImprove,
                recommendations,
                modelResponse.modelVersion(),
                modelResponse.scoringMethod(),
                modelResponse.note()
        );
     }

}
