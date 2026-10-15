package com.forensiq.marg.assessment.client;

import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component 
public class Model1Client {
private final  RestTemplate restTemplate;

    Model1Client(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
public  NextQuestionResponse nextQuestionResponse(List<ResponseData> responseDatas){
     Model1Request request =
                new Model1Request(responseDatas);

        return restTemplate.postForObject(
                "http://localhost:8000/api/ml/assessment/next-question",
                request,
                NextQuestionResponse.class
        );
    }
 public record Model1Request(
            List<ResponseData> responses
    ) {}

    public record ResponseData(
            String question_id,
            String skill,
            int correct
    ) {}

    public record NextQuestionResponse(
            String question_id,

        String skill,

        String topic,

        String question_text,

        String option_a,

        String option_b,

        String option_c,

        String option_d,

        Map<String, Double> competency
    ) {}
}
