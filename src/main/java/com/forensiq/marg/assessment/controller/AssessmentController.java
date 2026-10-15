package com.forensiq.marg.assessment.controller;


import java.util.UUID;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forensiq.marg.assessment.dto.AnswerRequest;
import com.forensiq.marg.assessment.dto.AnswerResponse;
import com.forensiq.marg.assessment.dto.AssessmentDetailsResponse;
import com.forensiq.marg.assessment.dto.StartAssessmentResponse;
import com.forensiq.marg.assessment.service.AssessmentService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/api/assessments")
public class AssessmentController {
private final AssessmentService assessmentService;

  AssessmentController(AssessmentService assessmentService) {
    this.assessmentService = assessmentService;
  }
@PostMapping ("/start")
public ResponseEntity<StartAssessmentResponse> start(Authentication authentication){
    UUID userId=UUID.fromString(authentication.getName());
    return ResponseEntity.ok(assessmentService.startAssessmentResponse(userId));
}
@PostMapping ("/{assessmentId}/answer")
public ResponseEntity<AnswerResponse> answer(@PathVariable  UUID assessmentId,@Valid @RequestBody AnswerRequest request,Authentication authentication){
    UUID userId=UUID.fromString(authentication.getName());
    return ResponseEntity.ok(assessmentService.answerQuestion(userId, assessmentId, request));
}
@GetMapping ("/{assessmentId}")
public ResponseEntity<AssessmentDetailsResponse> assessmentDetails(@PathVariable UUID assessmentId,Authentication authentication){
    UUID userId=UUID.fromString(authentication.getName());
    return ResponseEntity.ok(assessmentService.getAssessment(userId, assessmentId));
}

}
