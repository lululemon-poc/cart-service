package com.lululemon.cart.acl.transformer;

import com.lululemon.cart.request.LineItemRequest;
import com.lululemon.cart.request.CartRequest;
import com.lululemon.cart.response.CartResponse;
import com.lululemon.cart.response.LineItemResponse;
import com.lululemon.cart.response.TotalPrice;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.*;
import io.sphere.sdk.client.BadRequestException;
import io.sphere.sdk.utils.MoneyImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


import javax.money.CurrencyUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CartTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(CartTransformer.class);

    /**
     * This method will transform cart requst into cart draft.
     *
     * @param cartRequest cart com.lululemon.cart.request.
     * @return CartDraft.
     */
    public Optional<CartDraft> transformToCartDraft(final CartRequest cartRequest) {
        log.debug("Transforming into CartDraft");
        CurrencyUnit currencyByCode = MoneyImpl.createCurrencyByCode("GBP");
        final Optional<List<LineItemDraft>> lineItemDrafts = transformToLineItemDrafts(cartRequest);
        if(lineItemDrafts.isEmpty()){
            return Optional.empty();
        }
        return Optional.ofNullable(CartDraftBuilder.of(currencyByCode)
                .country(CountryCode.GB)
                .lineItems(lineItemDrafts.get())
                .build());

    }

    public Optional<List<LineItemDraft>> transformToLineItemDrafts(CartRequest cartRequest) {
        if(!ObjectUtils.allNotNull(cartRequest) && CollectionUtils.isEmpty(cartRequest.getLineItems())){
            return Optional.ofNullable(null);
        }
        List<LineItemDraft> lineItemDrafts = cartRequest.getLineItems().stream()
                .map(lineItemRequest -> transformToLineItemDraft(lineItemRequest).get())
                .collect(Collectors.toList());

        return Optional.ofNullable(lineItemDrafts);
    }

    public Optional<LineItemDraft> transformToLineItemDraft(final LineItemRequest lineItemRequest) {
        LineItemDraftDsl lineItemDraft = LineItemDraftBuilder.ofSku(lineItemRequest.getSkuCode(),
                lineItemRequest.getQuantity()).build();
        return Optional.ofNullable(lineItemDraft);
    }

    public CartResponse transformToCartResponse(final Cart cart) {
        LOG.info("Transforming CT Cart into CartResponse for cart id {} ",cart.getId());
        CartResponse cartResponse= new CartResponse();
        cartResponse.setId(cart.getId());
        cartResponse.setDeliveryType("ship");
        setTotalPrice(cart, cartResponse);
        setLineItem(cart,cartResponse);
        LOG.info("Cart {} response has been transformed.",cart.getId());
        return cartResponse;
    }

    private void setLineItem(Cart cart, CartResponse cartResponse) {
        List<LineItemResponse> lineItems = cart.getLineItems().stream()
                .map(lineItem -> tranformToLineItemResponse(lineItem))
                .collect(Collectors.toList());
        cartResponse.setLineItems(lineItems);
    }

    private LineItemResponse tranformToLineItemResponse(final LineItem lineItem) {
        LineItemResponse lineItemResponse = new LineItemResponse();
        lineItemResponse.setId(lineItem.getId());
        lineItemResponse.setProductId(lineItem.getProductId());
        lineItemResponse.setProductName(lineItem.getName().toString());
        return lineItemResponse;
    }

    private void setTotalPrice(Cart cart, CartResponse cartResponse) {
        TotalPrice price = new TotalPrice();
        price.setCurrencyCode(cart.getTotalPrice().getCurrency().getCurrencyCode());
        price.setTotalPrice(cart.getTotalPrice().getNumber().doubleValue());
        cartResponse.setTotalPrice(price);
    }
}
