package com.forensiq.marg.skill.client;


import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component 
public class Model4Client {
@Value("${ml.model4.base-url}")
private String model4Clinet;
private final RestTemplate restTemplate;

    Model4Client(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public record Model4Request(
            String jobTitle,
            String jobDescription,
            int topN
    ) {
    }
    public record Model4Response(
            String targetRole,
            List<Model4Course> recommendations,
            String modelVersion,
            String scoringMethod,
            String note

    ) {
    }

    public record Model4Course(
            @JsonProperty("course_title")
            String courseTitle,
            @JsonProperty("course_description")
            String courseDescription,
            @JsonProperty("course_url")
            String courseUrl,
            @JsonProperty("similarity_score")
            double similarityScore

    ) {
    
}
   public Model4Response courseRecomendation(  String jobTitle,
            String jobDescription,
            int topN){
  String url=model4Clinet+"/api/ml/recommendations";
  Model4Request request=new Model4Request(jobTitle, jobDescription, topN);
 HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.APPLICATION_JSON);
 HttpEntity<Model4Request>  request1=new HttpEntity<>(request,headers);
 try {
    Model4Response response=restTemplate.postForObject(url, request1, Model4Response.class);
    if (response == null || response.recommendations() == null) {

                throw new ResponseStatusException(  HttpStatus.BAD_GATEWAY,
                        "Model 4 returned an invalid response"
                );
            }
            return response;
 } catch (Exception e) {
      throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Unable to communicate with Model 4",
                    e
            );
 }
   } 
}
