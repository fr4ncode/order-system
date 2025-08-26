package dev.francode.ordersystem.entity.base;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;

    @CreatedDate
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "fecha_actualizacion")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "creado_por", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "actualizado_por")
    private String updatedBy;
}
