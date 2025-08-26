package dev.francode.ordersystem.dto.order;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailsResponse {

    private Long id;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}
