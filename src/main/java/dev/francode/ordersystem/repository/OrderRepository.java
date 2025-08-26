package dev.francode.ordersystem.repository;

import dev.francode.ordersystem.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
}

