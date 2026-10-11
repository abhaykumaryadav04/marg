package com.forensiq.marg.skill.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forensiq.marg.skill.dto.SkillExtractionRequest;
import com.forensiq.marg.skill.dto.SkillExtractionResponse;
import com.forensiq.marg.skill.dto.SkillGapAnalysisRequest;
import com.forensiq.marg.skill.dto.SkillGapAnalysisResponse;
import com.forensiq.marg.skill.service.SkillAnalysisService;
import com.forensiq.marg.skill.service.SkillExtractionService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/api/skills")
public class SkillExtractionController {
      @Autowired 
    private SkillAnalysisService skillAnalysisService;
    @Autowired 
    private SkillExtractionService skillExtractionService;
    @PostMapping ("/extract")
    public ResponseEntity<SkillExtractionResponse> getSkills(@RequestBody @Valid  SkillExtractionRequest request){
        return ResponseEntity.ok(skillExtractionService.extractSkills(request));
    }
      @PostMapping ("/analyze")
    public  ResponseEntity< SkillGapAnalysisResponse> analysis(@RequestBody  @Valid SkillGapAnalysisRequest request,Authentication authentication){
        UUID userId=UUID.fromString(authentication.getName());
        return ResponseEntity.ok(skillAnalysisService.getSkillGapAnalysis(request,userId));
    }
  
}
