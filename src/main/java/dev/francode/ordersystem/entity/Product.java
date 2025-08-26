package dev.francode.ordersystem.entity;

import dev.francode.ordersystem.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Productos")
public class Product extends BaseEntity<Long> {

    @Column(name = "nombre", nullable = false, unique = true)
    private String name;

    @Column(name = "descripcion")
    private String description;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "precio_descuento", precision = 10, scale = 2)
    private BigDecimal priceDiscount;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "marca", nullable = false)
    private String brandName;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Image> images;
}
