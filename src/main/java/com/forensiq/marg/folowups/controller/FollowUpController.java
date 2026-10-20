package com.forensiq.marg.folowups.controller;


import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forensiq.marg.folowups.dto.FollowUpRequestResponse;
import com.forensiq.marg.folowups.dto.FollowUpResponse;
import com.forensiq.marg.folowups.service.FollowUpService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/trainees/me/follow-ups")
@Validated
public class FollowUpController {

    private final FollowUpService followUpService;

    FollowUpController(FollowUpService followUpService) {
        this.followUpService = followUpService;
    }
   @GetMapping
    public ResponseEntity<List<FollowUpResponse>> getMyFollowUps( Authentication authentication) {

        UUID traineeId = UUID.fromString(authentication.getName() );
        List<FollowUpResponse> followUps = followUpService.getFollowUpResponse(traineeId);
        return ResponseEntity.ok(followUps);
    }
      @GetMapping("/{followUpId}")
    public ResponseEntity<FollowUpResponse> getMyFollowUp( @PathVariable UUID followUpId, Authentication authentication) {

        UUID traineeId = UUID.fromString(authentication.getName());
        FollowUpResponse response = followUpService.getMyFollowUp(traineeId, followUpId);
        return ResponseEntity.ok(response);
    }
      @PostMapping("/{followUpId}/response")
    public ResponseEntity<FollowUpResponse> submitResponse(
            @PathVariable UUID followUpId,
            Authentication authentication,
            @Valid @RequestBody FollowUpRequestResponse request
    ) {

        UUID traineeId = UUID.fromString(authentication.getName());
        FollowUpResponse response =followUpService.submit(traineeId,followUpId, request);
        return ResponseEntity.ok(response);
    }
}
