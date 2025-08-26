package dev.francode.ordersystem.dto.order;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotEmpty(message = "Debe agregar al menos un producto al pedido.")
    private List<OrderDetailsRequest> products;

    public void validateNoDuplicateProducts() {
        Set<Long> productIds = new HashSet<>();
        for (OrderDetailsRequest item : products) {
            if (!productIds.add(item.getProductId())) {
                throw new IllegalArgumentException("No se puede repetir el producto con ID " + item.getProductId() + " en el pedido.");
            }
        }
    }
}
