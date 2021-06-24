package com.lululemon.cart.acl.facade.impl;

import com.lululemon.cart.acl.facade.CartFacade;
import com.lululemon.cart.client.CommercetoolsClient;
import com.lululemon.cart.config.CommercetoolsConfigurationReader;
import com.lululemon.cart.exception.CTServerException;
import com.lululemon.cart.exception.CartNotFoundException;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.carts.commands.CartDeleteCommand;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddLineItem;
import io.sphere.sdk.carts.commands.updateactions.ChangeLineItemQuantity;
import io.sphere.sdk.carts.commands.updateactions.RemoveLineItem;
import io.sphere.sdk.carts.queries.CartByIdGet;
import io.sphere.sdk.client.SphereRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class CommercetoolsCartFacade implements CartFacade {

    @Autowired
    private CommercetoolsClient client;

    @Autowired
    private CommercetoolsConfigurationReader configurationReader;

    public Optional<Cart> createCartAndAddLineItems(final CartDraft cartDraft) {
        CartCreateCommand cartCreateCommand = CartCreateCommand.of(cartDraft);
        log.info("Calling commercetools for cart creation.");
        long start = System.currentTimeMillis();
        Cart cart = executeAction(cartCreateCommand);
        long elapsedTime = System.currentTimeMillis() - start;
        log.info("Cart ID {} created in commercetools in {} ms.", cart.getId(), elapsedTime);
        return Optional.ofNullable(cart);
    }

    public Optional<Cart> findCartById(final String cartId) {
        log.info("Calling commercetools for fetching cart details for cart id : {}", cartId);
        long start = System.currentTimeMillis();
        Cart cart = executeAction(CartByIdGet.of(cartId));
        if (cart == null){
            throw new CartNotFoundException("Cart not found for given cart id : "+cartId);
        }
        long elapsedTime = System.currentTimeMillis() - start;
        log.info("Cart ID {} fetched in commercetools in {} ms.", cart.getId(), elapsedTime);
        return Optional.ofNullable(cart);
    }

    public Optional<Cart> deleteCart(final String cartId) {
        Cart cart = findCartById(cartId).orElseThrow(() -> new CartNotFoundException("Cart not found for given cart id {} " + cartId));
        log.info("Calling commercetools for removing cart id : {}", cartId);
        long start = System.currentTimeMillis();
        Cart deletedCart = executeAction(CartDeleteCommand.of(cart));
        long elapsedTime = System.currentTimeMillis() - start;
        log.info("Cart ID {} deleted in commercetools in {} ms.", deletedCart.getId(), elapsedTime);
        return Optional.ofNullable(deletedCart);
    }

    @Override
    public Optional<Cart> addLineItems(final Cart cart, final List<AddLineItem> lineItemDrafts) {
        log.info("Calling commercetools for adding line items in given cart id : {}.", cart.getId());
        CartUpdateCommand cartUpdateCommand = CartUpdateCommand.of(cart, lineItemDrafts);
        long start = System.currentTimeMillis();
        Cart updatedCart = executeAction(cartUpdateCommand);
        long elapsedTime = System.currentTimeMillis() - start;
        log.info("LineItems added in given cart ID {} in commercetools in {} ms.", updatedCart.getId(), elapsedTime);
        return Optional.ofNullable(updatedCart);
    }

    @Override
    public Optional<Cart> updateLineItemQuantity(final Cart cart, final ChangeLineItemQuantity changeLineItemQuantity) {
        log.info("Calling commercetools for updating line items quantity for given cart id {}.", cart.getId());
        CartUpdateCommand cartUpdateCommand = CartUpdateCommand.of(cart, changeLineItemQuantity);
        long start = System.currentTimeMillis();
        Cart updatedCart = executeAction(cartUpdateCommand);
        long elapsedTime = System.currentTimeMillis() - start;
        log.info("LineItems added in given cart ID {} in commercetools in {} ms.", updatedCart.getId(), elapsedTime);
        return Optional.ofNullable(updatedCart);
    }

    @Override
    public Optional<Cart> removeLineItem(final Cart cart, final RemoveLineItem removeLineItem) {
        log.info("Calling commercetools for removing line items from cart id {}", cart.getId());
        CartUpdateCommand cartUpdateCommand = CartUpdateCommand.of(cart, removeLineItem);
        long start = System.currentTimeMillis();
        Cart updatedCart = executeAction(cartUpdateCommand);
        long elapsedTime = System.currentTimeMillis() - start;
        log.info("LineItems removed from cart ID {} in commercetools in {} ms.", updatedCart.getId(), elapsedTime);
        return Optional.ofNullable(updatedCart);
    }

    /**
     * This method accept cart command and persist it into ct.
     *
     * @param request cart command.
     * @return generic object.
     */
    public <T> T executeAction(final SphereRequest<T> request) {
        T t = null;
        try {
            t = client.getClient(configurationReader).execute(request).toCompletableFuture().get();
        } catch (ExecutionException ex) {
            log.error("Exception occurred while calling commercetools : {} ", ex.getMessage(), ex);
            throw new CTServerException(ex.getMessage(), ex.getCause());
        } catch (InterruptedException ex) {
            log.error("Exception occurred while calling commercetools : {} ", ex.getMessage(), ex);
            Thread.currentThread().interrupt();
            throw new CTServerException(ex.getMessage(), ex.getCause());
        }
        return t;
    }
}
