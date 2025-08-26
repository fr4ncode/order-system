package dev.francode.ordersystem.entity;

import dev.francode.ordersystem.entity.base.BaseEntity;
import dev.francode.ordersystem.entity.enums.ERole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Usuarios")
public class UserApp extends BaseEntity<Long> {

    @Column(name = "correo", nullable = false, unique = true)
    @Email
    private String email;

    @Column(name = "contraseña", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private ERole rol;

    public boolean isAdmin() {
        return this.rol == ERole.ADMIN;
    }
}