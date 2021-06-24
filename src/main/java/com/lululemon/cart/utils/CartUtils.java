package com.lululemon.cart.utils;

import com.lululemon.cart.constant.CartActionConstant;
import com.lululemon.cart.exception.BadRequestException;
import com.lululemon.cart.exception.InactiveCartException;
import com.lululemon.cart.exception.LineItemNotFoundException;
import com.lululemon.cart.request.CartRequest;
import io.sphere.sdk.carts.Cart;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.lululemon.cart.constant.CartActionConstant.*;

public class CartUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CartUtils.class);

    public static boolean validateCartAction(String requestedAction, String actualAction) {
        if (!StringUtils.isEmpty(requestedAction) && StringUtils.equals(requestedAction, actualAction)) {
            return true;
        }
        throw new BadRequestException("Invalid cart action.");
    }

    public static boolean isCartActive(final Cart cart) {
        if (CartActionConstant.ACTIVE.equalsIgnoreCase(cart.getCartState().toString())) {
            return true;
        }
        LOG.debug("Cart id {} is not active." + cart.getId());
        throw new InactiveCartException("Requested Cart ID : {} is not active." + cart.getId());
    }

    public static boolean validateLineItemId(final Cart cart, final String lineItemId) {
        cart.getLineItems().stream()
                .filter(lineItem -> StringUtils.equals(lineItem.getId(), lineItemId))
                .findAny()
                .orElseThrow(() -> new LineItemNotFoundException("Line item not found for given line item id : " + lineItemId));
        return true;
    }

    public static void isValidAction(final CartRequest cartRequest) {
        if (null == cartRequest || StringUtils.isEmpty(cartRequest.getAction())) {
            throw new BadRequestException("Invalid cart request.");
        }
        switch (cartRequest.getAction()) {
            case CREATE_CART:
                break;
            case UPDATE_LINE_ITEM_QUANTITY:
                break;
            case REMOVE_LINE_ITEM:
                break;
            case ADD_LINE_ITEM:
                break;
            default:
                throw new BadRequestException("Invalid cart Action. Valid cart action : createCart/removeLineItem/addLineItem/updateLineItemQuantity");
        }
    }
}
