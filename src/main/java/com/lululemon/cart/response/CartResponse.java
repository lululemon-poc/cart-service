package com.lululemon.cart.response;

import lombok.Data;

import java.util.List;

@Data
public class CartResponse {
    private String id;
    private String deliveryType;
    private List<LineItemResponse> lineItems;
    private TotalPrice totalPrice;
}
