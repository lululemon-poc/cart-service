package com.lululemon.cart.response;

import lombok.Data;

@Data
public class TotalPrice {
    private String currencyCode;
    private double totalPrice;
}
