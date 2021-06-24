package com.lululemon.cart.command.impl;

import com.lululemon.cart.acl.adaptor.CartAdaptor;
import com.lululemon.cart.constant.CartActionConstant;
import com.lululemon.cart.command.CartCommand;
import com.lululemon.cart.exception.BadRequestException;
import com.lululemon.cart.response.CartResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lululemon.cart.request.CartRequest;

import static com.lululemon.cart.utils.CartUtils.validateCartAction;

@Service(CartActionConstant.REMOVE_LINE_ITEM)
@Slf4j
public class RemoveLineItemCommand implements CartCommand {

    @Autowired
    private CartAdaptor cartAdaptor;

    @Override
    public CartResponse execute(final CartRequest request) {
        validateRequest(request);
        log.info("Cart : {} request validated for removing line item : {}", request.getId(), request.getLineItem().getLineItemId());
        CartResponse cartResponse = cartAdaptor.removeLineItem(request);
        return cartResponse;
    }

    private void validateRequest(final CartRequest request) {
        if (request == null || request.getLineItem() == null) {
            throw new BadRequestException("Invalid cart : " + request.getId() + " request for removing line item.");
        }
        validateCartAction(request.getAction(), CartActionConstant.REMOVE_LINE_ITEM);
        if (StringUtils.isEmpty(request.getLineItem().getLineItemId())) {
            throw new BadRequestException("Invalid request for removing line item.");
        }
    }
}