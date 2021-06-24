package com.lululemon.cart.command.impl;

import com.lululemon.cart.acl.adaptor.CartAdaptor;
import com.lululemon.cart.constant.CartActionConstant;
import com.lululemon.cart.command.CartCommand;
import com.lululemon.cart.exception.BadRequestException;
import com.lululemon.cart.response.CartResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lululemon.cart.request.*;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

import static com.lululemon.cart.utils.CartUtils.validateCartAction;

@Service(CartActionConstant.CREATE_CART)
@Slf4j
public class CreateCartCommand implements CartCommand {

    @Autowired
    private CartAdaptor cartAdaptor;


    @Override
    public CartResponse execute(final CartRequest request) {
        validateRequest(request);
        log.info("Request : {} validated for creating cart and adding line item", request.getLineItems());
        CartResponse cartResponse = Optional.ofNullable(cartAdaptor.createCartAndAddLineItems(request)).
                orElseThrow(() -> new BadRequestException("Invalid Exception"));
        log.info("Cart {} created and line items added successfully.", cartResponse.getId());
        return cartResponse;
    }

    private void validateRequest(final CartRequest request) {
        if (request == null) {
            throw new BadRequestException("Cart request cannot be null.");
        }
        if (CollectionUtils.isEmpty(request.getLineItems())) {
            throw new BadRequestException("Line item request cannot be null.");
        }
        validateCartAction(request.getAction(), CartActionConstant.CREATE_CART);
        request.getLineItems().forEach(
                lineItemRequest -> {
                    if (StringUtils.isEmpty(lineItemRequest.getSkuCode())) {
                        throw new BadRequestException("SKU code cannot be null or blank.");
                    }
                    if (lineItemRequest.getQuantity() == null || lineItemRequest.getQuantity() < 1) {
                        throw new BadRequestException("Quantity cannot be null, zero or negative.");
                    }
                }
        );
    }
}
