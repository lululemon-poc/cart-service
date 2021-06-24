package com.lululemon.cart.acl.adaptor.impl;

import com.lululemon.cart.acl.adaptor.CartAdaptor;
import com.lululemon.cart.acl.facade.CartFacade;
import com.lululemon.cart.acl.transformer.CartTransformer;
import com.lululemon.cart.exception.BadRequestException;
import com.lululemon.cart.exception.CTServerException;
import com.lululemon.cart.exception.CartNotFoundException;
import com.lululemon.cart.request.CartRequest;
import com.lululemon.cart.response.CartResponse;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.LineItemDraft;
import io.sphere.sdk.carts.commands.updateactions.AddLineItem;
import io.sphere.sdk.carts.commands.updateactions.ChangeLineItemQuantity;
import io.sphere.sdk.carts.commands.updateactions.RemoveLineItem;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.lululemon.cart.utils.CartUtils.isCartActive;
import static com.lululemon.cart.utils.CartUtils.validateLineItemId;

@Component
@Slf4j
public class CommercetoolsCartAdaptor implements CartAdaptor {

    @Autowired
    private CartTransformer cartTransformer;

    @Autowired
    private CartFacade cartFacade;

    public CartResponse createCartAndAddLineItems(CartRequest cartRequest) {
        Optional<CartDraft> cartDraft = cartTransformer.transformToCartDraft(cartRequest);
        log.info("CartDraft request has been transformed.");
        if (cartDraft.isEmpty()) {
            log.debug("Invalid cart request : {} " + cartRequest);
            throw new BadRequestException("Invalid cart request.");
        }
        log.info("Calling CT facade for creating cart and adding line items.");
        Cart cart = cartFacade.createCartAndAddLineItems(cartDraft.get()).orElseThrow(() -> new CTServerException("Exception occurred while saving cart into commercetools."));
        return cartTransformer.transformToCartResponse(cart);
    }

    public CartResponse findCartById(final String cartId) {
        log.info("Calling CT facade for fetching cart by id : {}", cartId);
        Cart cart = cartFacade.findCartById(cartId).orElseThrow(() -> new CartNotFoundException("Cart not found for given cart id {} " + cartId));
        return cartTransformer.transformToCartResponse(cart);
    }

    public CartResponse deleteCartById(final String cartId) {
        log.info("Calling CT facade for removing cart {}" + cartId);
        Optional<Cart> deletedCart = cartFacade.deleteCart(cartId);
        return new CartResponse();
    }

    @Override
    public CartResponse addLineItem(final CartRequest request) {
        Cart cart = cartFacade.findCartById(request.getId()).orElseThrow(() -> new CartNotFoundException("Cart not found for given cart id {} " + request.getId()));
        List<LineItemDraft> lineItemDrafts = cartTransformer.transformToLineItemDrafts(request).orElseThrow(() ->
                new BadRequestException("Exception occurred while transforming cart request to LineItemDraft."));
        List<AddLineItem> addLineItems = lineItemDrafts.stream().
                map(lineItemDraft -> AddLineItem.of(lineItemDraft))
                .collect(Collectors.toList());
        log.info("Transformed into AddLineDraft for adding line items in the cart id : {}.", cart.getId());
        Cart updatedCart = cartFacade.addLineItems(cart, addLineItems).orElseThrow(() -> new CTServerException("Exception occurred while saving cart into commercetools."));
        return cartTransformer.transformToCartResponse(updatedCart);
    }

    @Override
    public CartResponse changeLineItemQuantity(CartRequest request) {
        Cart cart = cartFacade.findCartById(request.getId()).orElseThrow(() -> new CartNotFoundException("Cart not found for given cart id {} " + request.getId()));
        isCartActive(cart);
        validateLineItemId(cart, request.getLineItem().getLineItemId());
        ChangeLineItemQuantity changeLineItemQuantity = ChangeLineItemQuantity.of(request.getLineItem().getLineItemId(), request.getLineItem().getQuantity());
        log.info("Transformed into ChangeLineItemQuantity for updating line item quantity in the cart id : {} .", cart.getId());
        Cart updatedCart = cartFacade.updateLineItemQuantity(cart, changeLineItemQuantity).orElseThrow(() -> new BadRequestException("Invalid LineItem Request"));
        ;
        return cartTransformer.transformToCartResponse(updatedCart);
    }

    @Override
    public CartResponse removeLineItem(final CartRequest request) {
        Cart cart = cartFacade.findCartById(request.getId()).orElseThrow(() -> new CartNotFoundException("Cart not found for given cart id {} " + request.getId()));
        isCartActive(cart);
        validateLineItemId(cart, request.getLineItem().getLineItemId());
        final RemoveLineItem removeLineItem = RemoveLineItem.of(request.getLineItem().getLineItemId());
        log.info("Transformed into RemoveLineItem for removing line item from the cart.");
        Cart updatedCart = cartFacade.removeLineItem(cart, removeLineItem).orElseThrow(() -> new BadRequestException("Invalid LineItem Request"));
        ;
        return cartTransformer.transformToCartResponse(updatedCart);
    }
}
