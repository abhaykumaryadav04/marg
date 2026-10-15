package com.forensiq.marg.skill.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.forensiq.marg.assessment.entity.AssessmentAttempt;
import com.forensiq.marg.assessment.entity.CompetencyScore;
import com.forensiq.marg.assessment.repo.AssessmentAttemptRepo;
import com.forensiq.marg.assessment.repo.CompetencyScoreRepo;
import com.forensiq.marg.skill.client.Model3Client;
import com.forensiq.marg.skill.dto.SkillGapAnalysisRequest;
import com.forensiq.marg.skill.dto.SkillGapAnalysisResponse;
import com.forensiq.marg.user.entity.TraineeProfile;
import com.forensiq.marg.user.repo.TraineeProfileRepo;

@Service 
public class SkillAnalysisService {
    private final AssessmentAttemptRepo assessmentAttemptRepo;
 private final  CompetencyScoreRepo competencyScoreRepo;
 private final Model3Client model3Client;
 private final TraineeProfileRepo traineeProfile;

  SkillAnalysisService(AssessmentAttemptRepo assessmentAttemptRepo, CompetencyScoreRepo competencyScoreRepo, TraineeProfileRepo traineeProfile, Model3Client model3Client) {
    this.assessmentAttemptRepo = assessmentAttemptRepo;
    this.competencyScoreRepo = competencyScoreRepo;
    this.traineeProfile = traineeProfile;
    this.model3Client = model3Client;
  }

  @Transactional(readOnly = true)
  public SkillGapAnalysisResponse getSkillGapAnalysis(SkillGapAnalysisRequest request,UUID userId){
    AssessmentAttempt attempt=assessmentAttemptRepo.findFirstByUser_IdOrderByStartedAtDesc(userId).orElseThrow(()->new RuntimeException(" Start an assessment before analyzing skill gaps"));

    List<CompetencyScore> scores=competencyScoreRepo.findByAssessmentId(attempt.getId());
      if (scores.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,"No competency scores found. Complete the competency integration first."
            );
        }
        Map<String ,Double> competencies=scores.stream().collect(Collectors.toMap(CompetencyScore::getSkill,CompetencyScore::getScore,(existing,replacement)->replacement));
        Model3Client.Model3Response modelResponse=model3Client.getSkillGap(request.targetRole().strip(), competencies);
        List<SkillGapAnalysisResponse.SkillGap> gaps =
                modelResponse.gaps() == null
                        ? List.of()
                        : modelResponse.gaps()
                                .stream()
                                .map(gap ->
                                        new SkillGapAnalysisResponse.SkillGap(
                                                gap.skill(),
                                                gap.currentScore(),
                                                gap.requiredScore(),
                                                gap.gapScore(),
                                                gap.importance(),
                                                gap.impactWeight(),
                                                gap.intelligentGapScore(),
                                                gap.priority()
                                        )
                                )
                                .toList();

        return new SkillGapAnalysisResponse(
                attempt.getId(),
                request.targetRole().strip(),
                gaps
        );

  }
  public SkillGapAnalysisResponse getMyskillGaps(UUID userId){
    TraineeProfile profile = traineeProfile
            .findByUserId(userId)
            .orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Create your trainee profile first"));

    String targetRole = profile.getTargetJobRole();

    if (targetRole == null || targetRole.isBlank()) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Set your target job role in your trainee profile first"
        );
    }

   
    return getSkillGapAnalysis(
            new SkillGapAnalysisRequest(targetRole.strip()),
            userId
    );
  }

}
