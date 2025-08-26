package dev.francode.ordersystem.controller;

import dev.francode.ordersystem.dto.order.OrderFilter;
import dev.francode.ordersystem.dto.order.OrderRequest;
import dev.francode.ordersystem.dto.order.OrderResponse;
import dev.francode.ordersystem.dto.order.OrderAdminFilter;
import dev.francode.ordersystem.service.interfaces.OrderService;
import dev.francode.ordersystem.service.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAuthority('CLIENTE')")
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderRequest orderRequest,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderResponse response = orderService.createOrder(orderRequest, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long orderId,
                                                     @RequestBody @Valid OrderRequest orderRequest,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderResponse response = orderService.updateOrder(orderId, orderRequest, userDetails.getId());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderService.cancelOrder(orderId, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @GetMapping("/customer")
    public ResponseEntity<Page<OrderResponse>> getUserOrders(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @ModelAttribute OrderFilter filter) {

        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> orders = orderService.getOrdersByUser(userDetails.getId(), filter, pageable);
        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @ModelAttribute OrderAdminFilter filter) {

        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> orders = orderService.getOrders(filter, pageable);
        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<Void> confirmOrder(@PathVariable Long orderId) {
        orderService.confirmOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{orderId}/send")
    public ResponseEntity<Void> sendOrder(@PathVariable Long orderId) {
        orderService.sendOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Void> deliverOrder(@PathVariable Long orderId) {
        orderService.deliverOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderResponse response = orderService.getOrderById(orderId, userDetails.getId());
        return ResponseEntity.ok(response);
    }
}