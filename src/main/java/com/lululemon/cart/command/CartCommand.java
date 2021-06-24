package com.lululemon.cart.command;

import com.lululemon.cart.response.CartResponse;
import com.lululemon.cart.request.*;


public interface CartCommand {

    CartResponse execute(CartRequest request);
}
