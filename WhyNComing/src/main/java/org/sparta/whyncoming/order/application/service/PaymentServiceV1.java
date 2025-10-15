package org.sparta.whyncoming.order.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.sparta.whyncoming.order.presentation.dto.reqeust.ConfirmPaymentRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.ConfirmPaymentResponseV1;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceV1 {

    @Value("${toss.secret-key}")
    private String secretKey;

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    // ✅ 1️⃣ 결제 READY (Toss 결제창 URL 요청)
    public ConfirmPaymentResponseV1 requestPayment() throws IOException {
        String orderId = UUID.randomUUID().toString();
        Long amount = 1000L;

        String apiUrl = "https://api.tosspayments.com/v1/payments/ready";

        JSONObject json = new JSONObject();
        json.put("orderId", orderId);
        json.put("orderName", "테스트 결제");
        json.put("amount", amount);
        json.put("successUrl", "http://localhost:8080/v1/order/payments/success");
        json.put("failUrl", "http://localhost:8080/v1/order/payments/fail");
        json.put("method", "CARD"); // ✅ 이거 반드시 필요함

        log.info("✅ Toss Request Body: {}", json.toJSONString());

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                json.toJSONString()
        );

        // ✅ Toss Basic Auth
        String authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes());

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("Authorization", authHeader)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = httpClient.newCall(request).execute();
        String responseBody = response.body().string();

        log.info("✅ Toss Ready Response: {}", responseBody);

        JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
        JSONObject jsonResponse;
        try {
            jsonResponse = (JSONObject) parser.parse(responseBody);
        } catch (ParseException e) {
            throw new RuntimeException("결제 READY JSON 파싱 실패", e);
        }

        // ✅ checkout.url.web 경로 추출
        JSONObject checkout = (JSONObject) jsonResponse.get("checkout");
        String checkoutUrl = checkout != null ? (String) checkout.get("url") : null;

        return new ConfirmPaymentResponseV1(checkoutUrl);
    }

    // ✅ 2️⃣ 결제 승인 (Redirect 이후 호출)
    public String confirmPayment(ConfirmPaymentRequestV1 request) throws IOException {
        String apiUrl = "https://api.tosspayments.com/v1/payments/confirm";

        JSONObject json = new JSONObject();
        json.put("paymentKey", request.getPaymentKey());
        json.put("orderId", request.getOrderId());
        json.put("amount", request.getAmount());

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                json.toJSONString()
        );

        String authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes());

        Request httpRequest = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("Authorization", authHeader)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = httpClient.newCall(httpRequest).execute();
        String responseBody = response.body().string();

        log.info("✅ Toss Confirm Response: {}", responseBody);

        JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
        JSONObject jsonResponse;
        try {
            jsonResponse = (JSONObject) parser.parse(responseBody);
        } catch (ParseException e) {
            throw new RuntimeException("결제 확인 JSON 파싱 실패", e);
        }

        String status = (String) jsonResponse.get("status");
        return "DONE".equals(status) ? "결제 승인 완료!" : "결제 상태: " + status;
    }
}