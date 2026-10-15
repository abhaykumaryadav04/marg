package com.forensiq.marg.user.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forensiq.marg.assessment.dto.CompetencyResponse;
import com.forensiq.marg.assessment.service.AssessmentService;

@RestController 
@RequestMapping ("/api/trainees/me")
public class TraineeCompetencyController {
    private final AssessmentService assessmentService;

    TraineeCompetencyController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @GetMapping 
    public ResponseEntity<CompetencyResponse> getMyCompetencyScore(Authentication authorization){
        UUID userId=UUID.fromString(authorization.getName());
        return ResponseEntity.ok(assessmentService.getMCompetencyResponse(userId));
    }

}
