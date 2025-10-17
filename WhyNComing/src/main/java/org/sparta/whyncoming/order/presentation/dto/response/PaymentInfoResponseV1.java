package org.sparta.whyncoming.order.presentation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentInfoResponseV1 {
    private String paymentKey;      // Toss 내부 결제 고유 키
    private String orderId;         // 우리 시스템 주문 ID (UUID)
    private String orderName;       // 주문명
    private String status;          // 결제 상태 (READY, IN_PROGRESS, DONE, FAILED 등)
    private String method;          // 결제 방식 (CARD, VIRTUAL_ACCOUNT 등)

    private Integer totalAmount;    // 총 결제 금액
    private String approvedAt;      // 결제 승인 일시
    private String requestedAt;     // 결제 요청 일시

    private CardInfo card;

    @Getter
    @NoArgsConstructor
    public static class CardInfo {
        private String company;        // 카드사 이름 (삼성, 현대, 국민 등)
        private String number;         // 카드 번호 (일부 마스킹)
        private Integer installmentPlanMonths;  // 할부 개월 수
        private String approveNo;      // 승인 번호
        private Boolean isInterestFree; // 무이자 여부

        public CardInfo(String company, String number, Integer installmentPlanMonths,
                        String approveNo, Boolean isInterestFree) {
            this.company = company;
            this.number = number;
            this.installmentPlanMonths = installmentPlanMonths;
            this.approveNo = approveNo;
            this.isInterestFree = isInterestFree;
        }
    }

    public PaymentInfoResponseV1(String paymentKey, String orderId, String orderName, String status, String method,
                                 Integer totalAmount, String approvedAt, String requestedAt, CardInfo card) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.orderName = orderName;
        this.status = status;
        this.method = method;
        this.totalAmount = totalAmount;
        this.approvedAt = approvedAt;
        this.requestedAt = requestedAt;
        this.card = card;
    }
}
