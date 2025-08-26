package dev.francode.ordersystem.service.interfaces;

import dev.francode.ordersystem.dto.order.OrderAdminFilter;
import dev.francode.ordersystem.dto.order.OrderFilter;
import dev.francode.ordersystem.dto.order.OrderRequest;
import dev.francode.ordersystem.dto.order.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest orderRequest, Long userId);

    OrderResponse createOrderWithDefaultUser(OrderRequest orderRequest);

    OrderResponse updateOrder(Long orderId, OrderRequest orderRequest, Long userId);

    OrderResponse getOrderById(Long orderId, Long userId);

    // Listar pedidos del cliente autenticado con filtro y paginación
    Page<OrderResponse> getOrdersByUser(Long userId, OrderFilter filter, Pageable pageable);

    // Listar pedidos (admin) con filtro y paginación
    Page<OrderResponse> getOrders(OrderAdminFilter filter, org.springframework.data.domain.Pageable pageable);
    // Acciones de estado
    void cancelOrder(Long orderId, Long userId);

    void confirmOrder(Long orderId);

    void sendOrder(Long orderId);

    void deliverOrder(Long orderId);

    OrderResponse getOrderByIdPublic(Long orderId);
    Page<OrderResponse> getDefaultUserOrders(OrderFilter filter, Pageable pageable);
}
