package dev.francode.ordersystem.dto.category;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {

    @NotBlank(message = "El nombre de la categoría no puede estar vacío.")
    @Size(max = 100, message = "El nombre de la categoría no puede tener más de 100 caracteres.")
    private String name;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres.")
    private String description;
}