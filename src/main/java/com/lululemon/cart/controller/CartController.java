package com.lululemon.cart.controller;

import com.lululemon.cart.command.CartCommand;
import com.lululemon.cart.request.CartRequest;
import com.lululemon.cart.response.CartResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.lululemon.cart.constant.CartActionConstant.DELETE_CART;
import static com.lululemon.cart.constant.CartActionConstant.GET_CART;

@RestController
@RequestMapping("/api/v1/carts")
@Slf4j
@Api(value = "Cart APIs", consumes = "application/json", produces = "application/json", tags = "Cart APIs")
public class CartController {

    @Autowired
    private Map<String, CartCommand> commandHandlersMap;

    /**
     * This method will create cart in commercetools
     * and add lineItems in the created cart.
     *
     * @param cartRequest cartRequest.
     * @return ResponseEntity<CartApiResponse>.
     */
    @Operation(summary = "Create cart and add line items", tags = {"Create Cart"},
            operationId = "createCartAndAddLineItems",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request for creating cart and adding line items", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CartRequest.class))
            })
            , responses =
            {
                    @ApiResponse(responseCode = "200", description = "Cart Response", content =
                            {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponse.class))
                            }),
                    @ApiResponse(responseCode = "404", description = "Invalid cart request.")
            })
    @PostMapping
    public ResponseEntity<CartResponse> createCartAndAddLineItems(@Valid @RequestBody final CartRequest cartRequest) {
        log.info("Processing request for creating cart and adding line items.");
        CartResponse response = commandHandlersMap.get(cartRequest.getAction()).execute(cartRequest);
        log.info("Request processed for creating cart and adding line items.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get Cart Details by cart id.
     *
     * @param cartId String
     * @return cartResponse {@link CartResponse}
     */
    @Operation(summary = "Get cart detail by id", tags = {"Get cart by ID."},
            operationId = "getCartByID", responses =
            {
                    @ApiResponse(responseCode = "200", description = "Cart Response", content =
                            {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponse.class))
                            }),
                    @ApiResponse(responseCode = "400", description = "Invalid Cart ID.")
            })
    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponse> getCartById(@PathVariable final String cartId) {
        log.info("Processing request for fetching cart details by id : {}", cartId);
        CartResponse cartResponse = commandHandlersMap.get(GET_CART).execute(new CartRequest(cartId));
        log.info("Request processed for fetching cart details by id : {}", cartId);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

    /**
     * Delete cart by ID.
     *
     * @param cartId String
     * @return responseEntity
     */
    @Operation(summary = "Delete cart by id", tags = {"Delete cart by ID."},
            operationId = "deleteCartByID", responses =
            {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", description = "Invalid Cart ID.")
            })
    @DeleteMapping("/{cartId}")
    public ResponseEntity<CartResponse> deleteCartById(@PathVariable final String cartId) {
        log.info("Processing request for removing cart id : {}", cartId);
        commandHandlersMap.get(DELETE_CART).execute(new CartRequest(cartId));
        log.info("Request processed for removing cart id : {}", cartId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Update Cart by id for
     * add/remove lineItems
     * update lineitem quantity
     *
     * @param cartId
     * @param cartRequest
     * @return {@link CartResponse}
     */
    @Operation(summary = "Update cart for add line items or remove line items or update line item quantity", tags = {"Update Cart."},
            operationId = "updateCart",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Update cart request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CartRequest.class))
            })
            , responses =
            {
                    @ApiResponse(responseCode = "200", description = "Cart Response", content =
                            {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponse.class))
                            }),
                    @ApiResponse(responseCode = "400", description = "Invalid Cart ID."),
                    @ApiResponse(responseCode = "404", description = "Invalid cart request."),
            })
    @PutMapping("/{cartId}")
    public ResponseEntity<CartResponse> updateCart(@PathVariable final String cartId, @Valid @RequestBody final CartRequest cartRequest) {
        log.info("Processing request for updating cart id : {}", cartId);
        cartRequest.setId(cartId);
        CartResponse response = commandHandlersMap.get(cartRequest.getAction()).execute(cartRequest);
        log.info("Request processed for updating cart id : {}", cartId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
