package com.forensiq.marg.skill.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forensiq.marg.skill.dto.SkillGapAnalysisResponse;
import com.forensiq.marg.skill.service.SkillAnalysisService;

@RestController 
@RequestMapping ("/api/trainees/me")
public class TraineeSkillController {
    @Autowired 
    private  SkillAnalysisService skillAnalysisService;

    TraineeSkillController(SkillAnalysisService skillAnalysisService) {
        this.skillAnalysisService = skillAnalysisService;
    }
      @GetMapping("/skill-gaps")
    public ResponseEntity<SkillGapAnalysisResponse> getMySkillGaps(
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        SkillGapAnalysisResponse response =skillAnalysisService.getMyskillGaps(userId);
        return ResponseEntity.ok(response);
    }

}
