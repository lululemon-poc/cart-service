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

@Service(CartActionConstant.UPDATE_LINE_ITEM_QUANTITY)
@Slf4j
public class ChangeLineItemQuantityCommand implements CartCommand {

    @Autowired
    private CartAdaptor cartAdaptor;

    @Override
    public CartResponse execute(final CartRequest request) {
        validateRequest(request);
        log.info("Cart {} request validated for updating line item id {} and quantity : {} ", request.getId(),
                request.getLineItem().getLineItemId(), request.getLineItem().getQuantity());
        CartResponse cartResponse = cartAdaptor.changeLineItemQuantity(request);
        return cartResponse;
    }

    private void validateRequest(final CartRequest request) {
        if (request == null || request.getLineItem() == null) {
            throw new BadRequestException("Invalid request for updating line item quantity.");
        }
        validateCartAction(request.getAction(), CartActionConstant.UPDATE_LINE_ITEM_QUANTITY);
        if (StringUtils.isEmpty(request.getLineItem().getLineItemId()) ||
                (request.getLineItem().getQuantity() == null || request.getLineItem().getQuantity() < 1)) {
            throw new BadRequestException("Invalid update line item quantity request.");
        }
    }
}