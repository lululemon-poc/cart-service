package com.lululemon.cart.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * The type Line item request.
 */
@Data
@Schema(description = "LineItem Request")
public class LineItemRequest {

    @Schema(description = "Line item id.")
    private String lineItemId;

    @Schema(description = "Sku code")
    @NotBlank(message = "SKU code cannot be blank or null.")
    private String skuCode;

    @Schema(description = "Quantity.")
    @Positive(message = "Quantity cannot be zero or negative")
    @NotNull(message = "Quantity cannot be null")
    private Long quantity;

    @Schema(description = "Line item type")
    private String itemType;
}

