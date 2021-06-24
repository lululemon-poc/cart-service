package com.lululemon.cart.command.impl;

import com.lululemon.cart.acl.adaptor.CartAdaptor;
import com.lululemon.cart.constant.CartActionConstant;
import com.lululemon.cart.command.CartCommand;
import com.lululemon.cart.response.CartResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lululemon.cart.request.CartRequest;

@Service(CartActionConstant.DELETE_CART)
@Slf4j
public class DeleteCartCommand implements CartCommand {

    @Autowired
    private CartAdaptor cartAdaptor;


    @Override
    public CartResponse execute(final CartRequest request) {
        cartAdaptor.deleteCartById(request.getId());
        return new CartResponse();
    }
}