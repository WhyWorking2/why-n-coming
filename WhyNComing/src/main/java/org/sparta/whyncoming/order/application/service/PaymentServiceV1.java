package org.sparta.whyncoming.order.application.service;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class PaymentServiceV1 {

    @Value("${toss.secret-key}")
    private String secretKey;

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    // 1️⃣ 결제 준비
    public ConfirmPaymentResponseV1 requestPayment() throws IOException {
        String orderId = UUID.randomUUID().toString();
        Long amount = 1000L;

        String apiUrl = "https://api.tosspayments.com/v1/payments/ready";

        // JSON body 생성
        JSONObject json = new JSONObject();
        json.put("orderId", orderId);
        json.put("orderName", "테스트 결제");
        json.put("amount", amount);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                json.toJSONString()
        );

        // Authorization 헤더 (Basic Auth)
        String authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes());

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("Authorization", authHeader)
                .build();

        Response response = httpClient.newCall(request).execute();
        String responseBody = response.body().string();

        // responseBody JSON 파싱
        JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
        JSONObject jsonResponse = (JSONObject) parser.parse(responseBody);
        String checkoutUrl = (String) jsonResponse.get("checkoutUrl");

        return new ConfirmPaymentResponseV1(checkoutUrl);
    }

    // 2️⃣ 결제 승인 확인 (결제 완료 후 redirect)
    public String confirmPayment(ConfirmPaymentRequestV1 request) throws IOException {
        String apiUrl = "https://api.tosspayments.com/v1/payments/" + request.getPaymentKey();

        String authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes());

        Request requestHttp = new Request.Builder()
                .url(apiUrl)
                .get()
                .addHeader("Authorization", authHeader)
                .build();

        Response response = httpClient.newCall(requestHttp).execute();
        String responseBody = response.body().string();

        JSONObject jsonResponse = JSONObject.parse(responseBody);

        if ("DONE".equals(jsonResponse.get("status"))) {
            return "결제 승인 완료!";
        } else {
            return "결제 처리 중: " + jsonResponse.get("status");
        }
    }
}