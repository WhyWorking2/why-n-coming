package org.sparta.whyncoming.product.application.service;

import org.sparta.whyncoming.product.domain.repository.ProductRepository;
import org.sparta.whyncoming.product.presentation.dto.response.ProductAiResponseDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductSimpleResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AiFoodRecommendationService {

    private final ProductRepository productRepository;
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${openai.api-key}")
    private String openAiApiKey;  // static 제거

    public AiFoodRecommendationService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductAiResponseDto recommendFood(@RequestParam String foodPreference) {
        if (foodPreference == null || foodPreference.isBlank()) {
            return ProductAiResponseDto.ofError("메시지를 입력해주세요!");
        }

        if (foodPreference.codePointCount(0, foodPreference.length()) > 15) {
            return ProductAiResponseDto.ofError("입력은 최대 15글자까지 가능합니다!");
        }

        // AI 호출
        String aiOutput = callOpenAi(foodPreference);

        // DB 상품 전부 불러오기
        List<String> aiFoodNames = extractAiFoodNames(aiOutput);

        // DB에서 매칭되는 상품 조회
        List<ProductSimpleResponseDto> matchedProducts = productRepository.findAllSimple().stream()
                .filter(p -> aiFoodNames.stream().anyMatch(name -> p.getProductName().contains(name)))
                .map(p -> new ProductSimpleResponseDto(p.getProductId(), p.getProductName()))
                .collect(Collectors.toList());

        return ProductAiResponseDto.ofSuccess(aiOutput, matchedProducts);

    }

    private String callOpenAi(String foodPreference) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini");
        body.put("temperature", 0.8);

        //AI 채팅 응답 방식을 위한 기초 설정
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system",
                "content", "너는 배달음식 전문가야. 사용자의 취향에 따라 현실적인 배달음식을 추천해줘. 추천할 땐 음식명과 간단한 설명만 줘."));
        messages.add(Map.of("role", "user", "content", foodPreference));
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

    /**
     * AI 텍스트에서 음식이름만 추출
     * 이 쪽 코드 때문에 AI 텍스트에서는 줄바꿈 안되는 문제가 있습니다만
     * 이 부분은 프론트에서 해결하면 되므로 굳이 추가 변경 안했습니다.
     * @param aiOutput AI 텍스트
     * @return 파싱된 AI 텍스트의 음식 이름들
     */
    private List<String> extractAiFoodNames(String aiOutput) {
        return Arrays.stream(aiOutput.split("\n"))
                .map(line -> line.replaceAll("^[0-9\\.\\-\\*\\s]+", "").trim()) // 앞 번호, 기호 제거
                .map(line -> line.split("-")[0].trim()) // 설명 제거, 음식 이름만
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }
}
