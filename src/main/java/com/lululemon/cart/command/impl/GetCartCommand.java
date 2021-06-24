package com.lululemon.cart.command.impl;

import com.lululemon.cart.acl.adaptor.CartAdaptor;
import com.lululemon.cart.constant.CartActionConstant;
import com.lululemon.cart.command.CartCommand;
import com.lululemon.cart.response.CartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lululemon.cart.request.CartRequest;

@Service(CartActionConstant.GET_CART)
public class GetCartCommand implements CartCommand {

    @Autowired
    private CartAdaptor cartAdaptor;

    @Override
    public CartResponse execute(CartRequest request) {
        CartResponse cartResponse = cartAdaptor.findCartById(request.getId());
        return cartResponse;
    }
}
