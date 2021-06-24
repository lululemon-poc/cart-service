package com.lululemon.cart.service;

import com.lululemon.cart.response.CartResponse;
import com.lululemon.cart.request.CartRequest;

public interface CartService {
    CartResponse getCartById(String cartId);
    void deleteCartById(String cartId);
}
