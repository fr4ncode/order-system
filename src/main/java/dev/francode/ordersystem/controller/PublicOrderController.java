package dev.francode.ordersystem.controller;

import dev.francode.ordersystem.dto.order.OrderFilter;
import dev.francode.ordersystem.dto.order.OrderRequest;
import dev.francode.ordersystem.dto.order.OrderResponse;
import dev.francode.ordersystem.service.interfaces.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class PublicOrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse createOrderWithDefaultUser(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.createOrderWithDefaultUser(orderRequest);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrderByIdPublic(@PathVariable Long orderId) {
        return orderService.getOrderByIdPublic(orderId);
    }
    @GetMapping
    public Page<OrderResponse> getDefaultUserOrders(
            @Valid OrderFilter filter,
            Pageable pageable) {
        return orderService.getDefaultUserOrders(filter, pageable);
    }
}
