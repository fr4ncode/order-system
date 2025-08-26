package dev.francode.ordersystem.dto.product;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "El nombre del producto no puede estar vacío.")
    @Size(max = 100, message = "El nombre del producto no puede tener más de 100 caracteres.")
    private String name;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres.")
    private String description;

    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.01", inclusive = true, message = "El precio debe ser mayor que 0.")
    @Digits(integer = 10, fraction = 2, message = "El precio debe tener hasta 2 decimales.")
    private BigDecimal price;

    @NotNull(message = "El precio de descuento es obligatorio.")
    @DecimalMin(value = "0.01", inclusive = true, message = "El precio de descuento debe ser mayor que 0.")
    @Digits(integer = 10, fraction = 2, message = "El precio de descuento debe tener hasta 2 decimales.")
    private BigDecimal priceDiscount;

    @NotNull(message = "El stock es obligatorio.")
    @Min(value = 0, message = "El stock no puede ser negativo.")
    private Integer stock;

    @NotNull(message = "La marca es obligatorio.")
    private String brandName;

    @NotNull(message = "El ID de la categoría es obligatorio.")
    private Long categoryId;
}
