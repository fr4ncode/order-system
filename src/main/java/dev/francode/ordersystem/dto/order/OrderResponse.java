package dev.francode.ordersystem.dto.order;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;

    private String userEmail;

    private BigDecimal total;

    private LocalDateTime date;

    private String status;

    private List<OrderDetailsResponse> orderDetails;
}
