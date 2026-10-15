package com.forensiq.marg.user.service;

import java.util.UUID;


import org.springframework.stereotype.Service;

import com.forensiq.marg.user.dto.TraineeProfileRequest;
import com.forensiq.marg.user.dto.TraineeProfileResponse;
import com.forensiq.marg.user.ennumeration.Role;
import com.forensiq.marg.user.entity.TraineeProfile;
import com.forensiq.marg.user.entity.User;
import com.forensiq.marg.user.repo.TraineeProfileRepo;
import com.forensiq.marg.user.repo.UserRepo;


@Service 
public class TraineeProfileService {
    private final  TraineeProfileRepo taineeProfileRepo;
    private final UserRepo userRepo;

    TraineeProfileService(TraineeProfileRepo taineeProfileRepo, UserRepo userRepo) {
        this.taineeProfileRepo = taineeProfileRepo;
        this.userRepo = userRepo;
    }

    private User getTraineeUser(UUID userId){
        User user=userRepo.findById(userId).orElseThrow(()-> new RuntimeException("User doesn't exists"));
        if(user.getRole()!=Role.TRAINEE){
            throw new RuntimeException("ONly user allow to do this operation");
        }
        return  user;
    }
    public TraineeProfileResponse getMyProfile(UUID userId){
        User user=getTraineeUser(userId);
        TraineeProfile traineeProfile=taineeProfileRepo.findByUserId(userId).orElseGet(()->createaEmptyProfile(user));
        return mapToReponse(user,traineeProfile);
    }
    public TraineeProfileResponse updateMyProfile(UUID userId,TraineeProfileRequest request){
        User user=getTraineeUser(userId);
        TraineeProfile profile=taineeProfileRepo.findByUserId(userId).orElseGet(()-> {TraineeProfile newprofile=TraineeProfile.builder().user(user).build();
              return taineeProfileRepo.save(newprofile);
        });
        profile.setSpecialization(request.specialization());
        profile.setQualification(request.qualification());
        profile.setCurrentJobRole(request.currentJobRole());
        profile.setDob(request.dateOfBirth());
        profile.setDistrict(request.district());
        profile.setEmploymentStatus(request.employmentStatus());
        profile.setExperienceYears(request.experienceYears());
        profile.setGender(request.gender());
        profile.setPhone(request.phone());
        profile.setState(request.state());
        profile.setTargetJobRole(request.targetJobRole());
        return mapToReponse(user, taineeProfileRepo.save(profile));


    }
    private TraineeProfileResponse mapToReponse(User user, TraineeProfile profile) {
            return new TraineeProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                profile.getPhone(),
                profile.getDob(),
                profile.getGender(),
                profile.getDistrict(),
                profile.getState(),
                profile.getQualification(),
                profile.getSpecialization(),
                profile.getExperienceYears(),
                profile.getEmploymentStatus(),
                profile.getCurrentJobRole(),
                profile.getTargetJobRole()
        );
    }
    private TraineeProfile createaEmptyProfile(User user) {
          TraineeProfile profile = TraineeProfile.builder()
                .user(user)
                .build();
        return  taineeProfileRepo.save(profile);
    }
    
    

}
