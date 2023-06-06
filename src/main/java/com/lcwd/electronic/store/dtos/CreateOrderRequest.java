package com.lcwd.electronic.store.dtos;


import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {

    @NotBlank(message = "Cart id is required")
    private String cartId;

    @NotBlank(message = "User id is required")
    private String userId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    @NotBlank(message = "billingAddress is required")
    private String billingAddress;
    @NotBlank(message = "billingPhone is required")
    private String billingPhone;
    @NotBlank(message = "billingName is required")
    private String billingName;

}
