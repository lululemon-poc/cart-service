package com.lululemon.cart.service.impl;

import com.lululemon.cart.acl.adaptor.impl.CommercetoolsCartAdaptor;
import com.lululemon.cart.response.CartResponse;
import com.lululemon.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CommercetoolsCartAdaptor commercetoolsCartAdaptor;

    @Override
    public CartResponse getCartById(String cartId) {
        CartResponse cartResponse = commercetoolsCartAdaptor.findCartById(cartId);
        return cartResponse;
    }

    @Override
    public void deleteCartById(String cartId) {
        commercetoolsCartAdaptor.deleteCartById(cartId);
    }
}
