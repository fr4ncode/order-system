package dev.francode.ordersystem.dto.order;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailsRequest {

    @NotNull(message = "El ID del producto es obligatorio.")
    private Long productId;

    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 1, message = "La cantidad mínima es 1.")
    private Integer quantity;
}