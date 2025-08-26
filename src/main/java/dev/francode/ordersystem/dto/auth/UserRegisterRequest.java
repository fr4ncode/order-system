package dev.francode.ordersystem.dto.auth;

import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequest {
    @NotBlank(message = "El correo electrónico no puede estar vacío.")
    @Email(message = "El formato del correo electrónico es inválido.")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía.")
    @Size(min = 6, max = 40, message = "La contraseña debe tener entre 6 y 40 caracteres.")
    private String password;

    @NotNull(message = "Los roles no pueden ser nulos.")
    @Pattern(regexp = "^(CLIENTE|ADMIN)$",
            message = "El rol del usuario debe ser CLIENTE o ADMIN")
    private String rol;
}
