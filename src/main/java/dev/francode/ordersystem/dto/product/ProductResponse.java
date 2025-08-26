package dev.francode.ordersystem.dto.product;

import dev.francode.ordersystem.dto.image.ImageResponse;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal priceDiscount;
    private Integer stock;
    private String brandName;
    private String categoryName;
    private String storeName;
    private List<ImageResponse> images;
}