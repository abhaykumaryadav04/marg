package com.forensiq.marg.assessment.service;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forensiq.marg.assessment.client.Model1Client;
import com.forensiq.marg.assessment.dto.AnswerRequest;
import com.forensiq.marg.assessment.dto.AnswerResponse;
import com.forensiq.marg.assessment.dto.AssessmentDetailsResponse;
import com.forensiq.marg.assessment.dto.CompetencyResponse;
import com.forensiq.marg.assessment.dto.StartAssessmentResponse;
import com.forensiq.marg.assessment.entity.AssessmentAttempt;
import com.forensiq.marg.assessment.entity.AssessmentResponse;
import com.forensiq.marg.assessment.entity.CompetencyScore;
import com.forensiq.marg.assessment.repo.AssessmentAttemptRepo;
import com.forensiq.marg.assessment.repo.AssessmentResponseRepo;
import com.forensiq.marg.assessment.repo.CompetencyScoreRepo;
import com.forensiq.marg.user.ennumeration.Role;
import com.forensiq.marg.user.entity.User;
import com.forensiq.marg.user.repo.UserRepo;

@Service 
public class AssessmentService {
    @Autowired 
private UserRepo userRepo;
@Autowired
private Model1Client model1Client;
@Autowired 
private AssessmentAttemptRepo assessmentAttemptRepo;
@Autowired 
private  AssessmentResponseRepo assessmentResponseRepo;
@Autowired 
private CompetencyScoreRepo competencyScoreRepo;

public  StartAssessmentResponse startAssessmentResponse(UUID userId){
    User user=userRepo.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
    if(user.getRole()!=Role.TRAINEE){
        throw new RuntimeException("Only Trainee allowed to perform this operation");
    }
    AssessmentAttempt attempt=AssessmentAttempt.builder().user(user)
                                                         .startedAt(LocalDateTime.now())
                                                         .status("IN_PROGRESS")
                                                         .build();
         AssessmentAttempt savedAttempt=  assessmentAttemptRepo.save(attempt);  
             Model1Client.NextQuestionResponse question=model1Client.nextQuestionResponse(Collections.emptyList());
             return new StartAssessmentResponse(
                  savedAttempt.getId(),
                question.question_id(),
                question.skill(),
                question.topic(),
                question.question_text(),
                question.option_a(),
                question.option_b(),
                question.option_c(),
                question.option_d()
             )  ;                                 
}
public AnswerResponse answerQuestion(UUID userId,UUID assessmentId,AnswerRequest request){
    AssessmentAttempt attempt=assessmentAttemptRepo.findByIdAndUserId(assessmentId, userId).orElseThrow(()-> new RuntimeException("Assessment can not exist"));
    if(!"IN_PROGRESS".equals(attempt.getStatus())){
        throw new RuntimeException("Assessment is already completed");
    }
    AssessmentResponse response=AssessmentResponse.builder().assessment(attempt)
                                                           .correct(request.correct())
                                                           .questionId(request.questionId())
                                                           .skill(request.skill())
                                                           .build();
 assessmentResponseRepo.save(response);
 List<AssessmentResponse> responses=assessmentResponseRepo.findByAssessmentId(assessmentId);
  List<Model1Client.ResponseData> modelResponses =
            responses.stream()  .map(r -> new Model1Client.ResponseData(
                                    r.getQuestionId(),
                                    r.getSkill(),  r.isCorrect() ? 1 : 0 )).toList();
      Model1Client.NextQuestionResponse nextQuestion =
            model1Client.nextQuestionResponse(modelResponses);
                saveCompetencyScores(
        attempt,
        nextQuestion.competency()
);
             return new AnswerResponse(
            assessmentId,
            request.questionId(),
            request.correct(),
            nextQuestion.question_id(),
            nextQuestion.skill(),
            nextQuestion.topic(),
            nextQuestion.question_text(),
            nextQuestion.option_a(),
            nextQuestion.option_b(),
            nextQuestion.option_c(),
            nextQuestion.option_d()
    );
}

public AssessmentDetailsResponse getAssessment(UUID userId,UUID assessmentId){
    AssessmentAttempt assessment=assessmentAttemptRepo.findByIdAndUserId(assessmentId, userId).orElseThrow(()-> new RuntimeException("Assessment does not exists"));
     List<AssessmentResponse> responses =
            assessmentResponseRepo
                    .findByAssessmentId(assessmentId);

    List<AssessmentDetailsResponse.AnswerDetails> answerDetails =
            responses.stream()
                    .map(response ->
                            new AssessmentDetailsResponse.AnswerDetails(
                                    response.getId(),
                                    response.getQuestionId(),
                                    response.getSkill(),
                                    response.isCorrect()
                            )
                    )
                    .toList();

        return new AssessmentDetailsResponse(
            assessment.getId(),
            assessment.getStatus(),
            assessment.getStartedAt(),
            assessment.getCompletedAt(),
            answerDetails
    );
}
public  void saveCompetencyScores(AssessmentAttempt assessmentAttempt,Map<String,Double> map){

   if (map == null || map.isEmpty()) {
        return;  
}
  map.forEach((skill, score) -> {
        CompetencyScore existing =
                competencyScoreRepo
                        .findByAssessmentIdAndSkill(
                                assessmentAttempt.getId(),
                                skill
                        )
                        .orElse(null);

 if (existing == null) {

            existing = CompetencyScore.builder()
                    .assessment(assessmentAttempt)
                    .skill(skill)
                    .theta(0.0)
                    .score(score)
                    .build();

        } else {

            existing.setScore(score);
        }

        competencyScoreRepo.save(existing);
    });
}
public  CompetencyResponse getMCompetencyResponse(UUID userId){
List<AssessmentAttempt> attempts=assessmentAttemptRepo.findAll().stream()
                                                      .filter(a->a.getUser().getId().equals(userId))
                                                      .filter(a->"IN_PROGRESS".equals(a.getStatus())
                                                    ||"COMPETED".equals(a.getStatus()))
                                                    .sorted( Comparator.comparing(
                                                       AssessmentAttempt::getStartedAt
                                                                ).reversed())
                                                                .toList();
     if (assessments.isEmpty()) {

        return new CompetencyResponse(
                null,
                "NOT_STARTED",
                List.of()
        );
    }
     AssessmentAttempt latest =
            attempts.get(0);

    List<CompetencyScore> scores =
            competencyScoreRepo
                    .findByAssessmentId(
                            latest.getId()
                    );

    List<CompetencyResponse.SkillCompetency>
            competencies =
            scores.stream()
                    .map(score ->
                            new CompetencyResponse.SkillCompetency(
                                    score.getSkill(),
                                    score.getScore()
                            )
                    )
                    .toList();

    return new CompetencyResponse(
            latest.getId(),
            latest.getStatus(),
            competencies
    );
}
}
