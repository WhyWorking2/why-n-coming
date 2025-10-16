package org.sparta.whyncoming.store.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateStoreOwnerRequestV1 {

    @NotBlank
    private String storeName;

    @NotBlank
    private String storeAddress;

    @NotBlank
    private String storePhone;

    private String storeContent;

    @NotNull
    @Min(0)
    private Integer minDeliveryPrice = 0;

    @NotNull
    @Min(0)
    private Integer deliveryTip = 0;

    private String operationHours;

    private String deliveryAddress;
}
