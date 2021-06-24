package com.lululemon.cart.request;

import com.lululemon.cart.validator.ValidAction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * The class Cart Request.
 */

@Data
@Schema(description = "Request for create or update cart.")
public class CartRequest {

    @Schema(description = "Cart ID.", example = "eb349b4d-5fb7-42c7-9491-d10ac14203e6")
    private String id;

    @Schema(description = "The list of update actions to be performed on the cart", allowableValues = "createCart or removeLineItem or addLineItem")
    @ValidAction
    @NotBlank(message = "Action cannot be empty or null")
    private String action;

    @Schema(description = "Request for adding line items in the cart.")
    @Valid
    private List<LineItemRequest> lineItems;

    @Schema(description = "Request for updating line items in the cart.")
    private LineItemRequest lineItem;

    @Schema(description = "Customer email.")
    private String customerEmail;
    private String channelCode;
    private String customerId;
    private String deliveryType;


    /**
     * Instantiates a new cart com.lululemon.cart.request.
     */
    public CartRequest() {
        super();
    }

    public CartRequest(String cartId) {
        this.id = cartId;
    }
}

