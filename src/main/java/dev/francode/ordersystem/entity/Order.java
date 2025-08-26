package dev.francode.ordersystem.entity;

import dev.francode.ordersystem.entity.base.BaseEntity;
import dev.francode.ordersystem.entity.enums.EStatusOrder;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Pedidos")
public class Order extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UserApp user;

    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EStatusOrder status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetails> OrderDetails;
}
