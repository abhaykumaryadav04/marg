package com.forensiq.marg.skill.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.forensiq.marg.skill.dto.SkillExtractionResponse;



@Component 
public class Model2Client {
    @Autowired 
    private   RestTemplate restTemplate;
  @Value("${ml.model2.base-url}")
  private String base2Url;

  Model2Client(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  } 
  
  public SkillExtractionResponse skillExtractionResponse(String text){
   String url =base2Url + "/api/ml/skill/predict";
   HttpHeaders headers=new HttpHeaders();
   headers.setContentType(MediaType.APPLICATION_JSON);
   Map<String,String> map=Map.of("text", text);
   HttpEntity<Map<String,String>> request=new HttpEntity<>(map,headers);
   try{
    SkillExtractionResponse response=restTemplate.postForObject(url, request, SkillExtractionResponse.class);

            if (response == null) {
                throw new IllegalStateException(
                        "Model 2 returned an empty response"
                );
            }

            return response;
   }catch(RestClientException exception){
       throw new IllegalStateException(
                    "Unable to communicate with Model 2 skill classification service",
                    exception
            );
   }
  }

}
