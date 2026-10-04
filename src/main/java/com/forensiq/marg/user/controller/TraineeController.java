package com.forensiq.marg.user.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forensiq.marg.user.dto.TraineeProfileRequest;
import com.forensiq.marg.user.dto.TraineeProfileResponse;
import com.forensiq.marg.user.service.TraineeProfileService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/api/trainee/me")
public class TraineeController {
@Autowired 
private  TraineeProfileService traineeProfileService;
@GetMapping 
public  ResponseEntity<TraineeProfileResponse> getMyProflie(Authentication authentication){
    UUID userId=UUID.fromString(authentication.getName());
    return ResponseEntity.ok(traineeProfileService.getMyProfile(userId));
}
@PutMapping 
public ResponseEntity<TraineeProfileResponse> updateMyProfile(  Authentication authentication,
            @Valid @RequestBody TraineeProfileRequest request){
                UUID userId=UUID.fromString(authentication.getName());
                return ResponseEntity.ok(traineeProfileService.updateMyProfile(userId, request));

}

}
