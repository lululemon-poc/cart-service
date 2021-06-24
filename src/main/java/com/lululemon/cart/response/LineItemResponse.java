package com.lululemon.cart.response;

import io.sphere.sdk.models.LocalizedString;
import lombok.Data;

@Data
public class LineItemResponse {

    private String id;
    private String productId;
    private String productKey;
    private String productName;
    private ProductSku sku;
    private Long quantity;
    private String itemType;
    private boolean bopis;

}
