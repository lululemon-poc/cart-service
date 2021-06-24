package com.lululemon.cart.acl.adaptor;

import com.lululemon.cart.response.CartResponse;
import com.lululemon.cart.request.CartRequest;

public interface CartAdaptor {
    CartResponse createCartAndAddLineItems(CartRequest cartRequest) ;
    CartResponse findCartById(String cartId) ;
    CartResponse deleteCartById(String cartId);
    CartResponse addLineItem(CartRequest request);
    CartResponse changeLineItemQuantity(CartRequest request);
    CartResponse removeLineItem(CartRequest request);
}
