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

        // AI 상품명만 파싱해서 리스트화
        List<String> aiFoodNames = extractAiFoodNames(aiOutput);

        // DB에서 매칭되는 상품 조회
        List<ProductSimpleResponseDto> matchedProducts = productRepository.findAllSimple().stream()
                .filter(p -> aiFoodNames.stream().anyMatch(name -> p.getProductName().contains(name)))
                .map(p -> new ProductSimpleResponseDto(p.getProductId(), p.getProductName()))
                .collect(Collectors.toList());

        return ProductAiResponseDto.ofSuccess(aiOutput, matchedProducts);

    }

    /**
     * OpenAI를 호출하는 메서드
     * @param foodPreference 사용자가 입력한 메서드
     * @return
     */
    private String callOpenAi(String foodPreference) {
    // RestTemplate 인스턴스 생성 및 HTTP 헤더 구성
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    /*
     * Authorization 헤더에 Bearer 토큰 형태로 OpenAI API 키를 삽입
     * openAiApiKey는 yml 파일을 통해 주입받은 상태
     */
    headers.setBearerAuth(openAiApiKey);

    /*
     * 요청 바디(페이로드)를 담을 맵 생성
     * OpenAI Chat Completions API의 기대 형식에 맞춰 구성
     * 사용될 AI 모델과 다양성(temperature) 정도를 0.0 ~ 2.0 사이로 지정 (값이 클수록 창의적)
      */
    Map<String, Object> body = new HashMap<>();
    body.put("model", "gpt-4o-mini");
    body.put("temperature", 0.8);

    /*
      messages 필드는 Chat API에서 역할(role)별 대화 이력을 배열로 받음
      여기서는 system 메시지와 user 메시지 2개를 보냅니다.
      system은 모델의 행동 지침을 설정 ('배달음식 전문가'로 동작하도록 지시)
     */
    List<Map<String, String>> messages = new ArrayList<>();
    messages.add(Map.of(
        "role", "system",
        "content", "너는 배달음식 전문가야. 사용자의 취향에 따라 현실적인 배달음식을 추천해줘. 추천할 땐 음식명과 간단한 설명만 줘."
    ));

    //user로 실제 사용자의 입력(음식 취향)을 전달하고 완성된 messages를 body에 넣음
    messages.add(Map.of("role", "user", "content", foodPreference));
    body.put("messages", messages);


    //HttpEntity는 헤더와 바디를 함께 담는 객체로, RestTemplate.exchange에 전달
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

    /*
     실제 API 호출:
     OPENAI_API_URL은 예: "https://api.openai.com/v1/chat/completions" 형태로 정의되어 있어야 합니다.
     exchange 메서드는 지정된 URL로 POST 요청을 보내고 응답을 Map 형태로 받습니다.
     ResponseEntity<Map>를 받기 때문에 response.getBody()는 Map 구조의 JSON 파싱 결과입니다.
     */

    ResponseEntity<Map> response = restTemplate.exchange(
            OPENAI_API_URL,   // 요청 URL
            HttpMethod.POST,  // HTTP 메서드
            entity,           // 헤더+바디
            Map.class         // 응답을 Map으로 매핑
    );

    // 응답에서 "choices"를 꺼내 List<Map<String, Object>> 형태로 캐스팅
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");

    // 첫 번째 choice의 "message"를 Map으로 꺼냄
    @SuppressWarnings("unchecked")
    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

    //message 내부의 "content"에 모델이 생성한 텍스트를 String으로 반환
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
