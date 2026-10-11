package com.forensiq.marg.skill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forensiq.marg.skill.client.Model2Client;
import com.forensiq.marg.skill.dto.SkillExtractionRequest;
import com.forensiq.marg.skill.dto.SkillExtractionResponse;

@Service 
public class SkillExtractionService {
    @Autowired 
    private Model2Client model2Client;
       public SkillExtractionResponse extractSkills(
            SkillExtractionRequest request
    ) {

        String text = request.text().strip();

        return model2Client.skillExtractionResponse(text);
    }
    
}
