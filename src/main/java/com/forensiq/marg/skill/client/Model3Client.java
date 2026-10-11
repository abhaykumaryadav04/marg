package com.forensiq.marg.skill.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonAlias;

@Component 
public class Model3Client {
@Autowired 
private RestTemplate restTemplate;

@Value ("{ml.model3.base-url}")
private String model3baseUrl;

public Model3Response getSkillGap( String targetRole,
            Map<String, Double> competencies){
  String url=model3baseUrl+"/api/ml/skill-gap";
   Model3Request body = new Model3Request(
                targetRole,
                competencies
        );
    HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Model3Request> request = new HttpEntity<>(body, headers);
      try {
            Model3Response response = restTemplate.postForObject(
                    url, request,
                    Model3Response.class
            );

            if (response == null) {
                throw new ResponseStatusException( HttpStatus.BAD_GATEWAY, "Model 3 returned an empty response");
            }
            return response;

        } catch (RestClientException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Unable to communicate with Model 3",
                    exception
            );
        }
            }
   public record Model3Request(

            String targetRole,

            Map<String, Double> competencies

    ) {
    }

    public record Model3Response(

            @JsonAlias("target_role")
            String targetRole,

            java.util.List<Model3Gap> gaps

    ) {
    }

    public record Model3Gap(

            String skill,

            @JsonAlias("current_score")
            double currentScore,

            @JsonAlias("required_score")
            double requiredScore,

            @JsonAlias("gap_score")
            double gapScore,

            double importance,

            @JsonAlias("impact_weight")
            double impactWeight,

            @JsonAlias("intelligent_gap_score")
            double intelligentGapScore,

            String priority

    ) {
    }
}

