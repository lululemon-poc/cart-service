package com.lululemon.cart.command.impl;

import com.lululemon.cart.acl.adaptor.CartAdaptor;
import com.lululemon.cart.constant.CartActionConstant;
import com.lululemon.cart.command.CartCommand;
import com.lululemon.cart.exception.BadRequestException;
import com.lululemon.cart.response.CartResponse;
import com.lululemon.cart.utils.CartUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lululemon.cart.request.*;
import org.springframework.util.CollectionUtils;

import static com.lululemon.cart.utils.CartUtils.validateCartAction;

@Service(CartActionConstant.ADD_LINE_ITEM)
@Slf4j
public class AddLineItemCommand implements CartCommand {

    @Autowired
    private CartAdaptor cartAdaptor;

    @Override
    public CartResponse execute(final CartRequest request) {
        validateRequest(request);
        log.info("Request : {} validated for adding line item in the cart : {} ", request.getLineItems(), request.getId());
        CartResponse cartResponse = cartAdaptor.addLineItem(request);
        return cartResponse;
    }

    private void validateRequest(final CartRequest request) {
        if (request == null) {
            throw new BadRequestException("Cart request cannot be null.");
        }
        if (CollectionUtils.isEmpty(request.getLineItems())) {
            throw new BadRequestException("Line item request cannot be null.");
        }
        validateCartAction(request.getAction(), CartActionConstant.ADD_LINE_ITEM);
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