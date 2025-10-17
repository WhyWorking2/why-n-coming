package org.sparta.whyncoming.product.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AiFoodRecommendationService {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${openai.api-key}")
    private String openAiApiKey;  // static 제거

    public String recommendFood(@RequestParam String text) {
        if(text.isEmpty()) return "메시지를 입력해주세요!";

        int length = text.codePointCount(0, text.length()); // 유니코드 기준 문자열 길이

        if (length > 15) {
            return "입력은 최대 15글자까지 가능합니다!";
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);  // 인스턴스 필드 사용

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini");
        body.put("temperature", 0.8);

        List<Map<String,String>> messages = new ArrayList<>();
        Map<String,String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", "너는 배달음식 전문가야. 사용자의 취향에 따라 현실적인 배달음식을 추천해줘. 추천할 땐 음식명과 간단한 설명만 줘.");
        messages.add(systemMsg);

        Map<String,String> userMsg = new HashMap<>();
        userMsg.put("role","user");
        userMsg.put("content", text);
        messages.add(userMsg);

        body.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                OPENAI_API_URL,
                HttpMethod.POST,
                entity,
                Map.class
        );

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        return message.get("content").toString();
    }
}
