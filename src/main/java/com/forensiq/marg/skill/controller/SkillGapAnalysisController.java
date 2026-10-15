package com.forensiq.marg.skill.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forensiq.marg.skill.dto.SkillGapAnalysisRequest;
import com.forensiq.marg.skill.dto.SkillGapAnalysisResponse;
import com.forensiq.marg.skill.service.SkillAnalysisService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/skill-gaps")
public class SkillGapAnalysisController {
    private final  SkillAnalysisService skillGapAnalysisService;

    SkillGapAnalysisController(SkillAnalysisService skillGapAnalysisService) {
        this.skillGapAnalysisService = skillGapAnalysisService;
    }
       @PostMapping("/analyze")
    public ResponseEntity<SkillGapAnalysisResponse> analyze( Authentication authentication,
            @Valid @RequestBody SkillGapAnalysisRequest request) {
        UUID userId = UUID.fromString(authentication.getName());
        SkillGapAnalysisResponse response = skillGapAnalysisService.getSkillGapAnalysis(request, userId);
        return ResponseEntity.ok(response);
    }

}
