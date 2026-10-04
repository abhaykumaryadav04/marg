package com.forensiq.marg.user.dto;

import java.util.UUID;

public record TraineeProfileResponse(

        UUID userId,

        String email,

        String fullName,

        String phone,

        String dateOfBirth,

        String gender,

        String district,

        String state,

        String qualification,

        String specialization,

        Integer experienceYears,

        String employmentStatus,

        String currentJobRole,

        String targetJobRole
) {

}
