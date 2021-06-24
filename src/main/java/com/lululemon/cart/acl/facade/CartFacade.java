package com.lululemon.cart.acl.facade;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.commands.updateactions.AddLineItem;
import io.sphere.sdk.carts.commands.updateactions.ChangeLineItemQuantity;
import io.sphere.sdk.carts.commands.updateactions.RemoveLineItem;
import io.sphere.sdk.client.SphereRequest;

import java.util.List;
import java.util.Optional;

public interface CartFacade {
    Optional<Cart> createCartAndAddLineItems(final CartDraft cartDraft);
    Optional<Cart> findCartById(String cartId) ;
    Optional<Cart> deleteCart(String cartId) ;
    <T> T executeAction(SphereRequest<T> request) ;
    Optional<Cart> addLineItems(Cart cart, List<AddLineItem> lineItemDrafts);
    Optional<Cart> updateLineItemQuantity(Cart cart, ChangeLineItemQuantity changeLineItemQuantity);
    Optional<Cart> removeLineItem(Cart cart, RemoveLineItem removeLineItem);
}
