package com.forensiq.marg.skill.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.forensiq.marg.skill.dto.CourseRecommendationResponse;
import com.forensiq.marg.skill.service.CourseRecommendationService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/trainees/me")
@Validated
public class CourseRecommendationController {

    private final CourseRecommendationService recommendationService;

    public CourseRecommendationController(
            CourseRecommendationService recommendationService
    ) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/course-recommendations")
    public ResponseEntity<CourseRecommendationResponse>
            getMyCourseRecommendations(

                    Authentication authentication,

                    @RequestParam(defaultValue = "5")
                    @Min(1)
                    @Max(20)
                    int topN

            ) {

        UUID userId = UUID.fromString(authentication.getName());

        CourseRecommendationResponse response =
                recommendationService.courseRecommendationResponse(
                        userId,
                        topN
                );

        return ResponseEntity.ok(response);
    }
}