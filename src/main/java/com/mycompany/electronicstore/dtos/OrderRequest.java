package com.mycompany.electronicstore.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OrderRequest {

    @NotBlank(message = "Cart id is required")
    private String cartId;
    @NotBlank(message = "User id is required")
    private String userId;
    private String orderStatus = "PENDING";
    private String paymentStatus = "NOTPAID";
    @NotBlank(message = "Billing address is required")
    private String billingAddress;
    @NotBlank(message = "Billing phone number is required")
    private String billingPhone;
    @NotBlank(message = "Billing name is required")
    private String billingName;
}
