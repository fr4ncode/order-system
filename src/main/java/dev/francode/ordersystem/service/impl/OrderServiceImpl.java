package dev.francode.ordersystem.service.impl;

import dev.francode.ordersystem.dto.order.*;
import dev.francode.ordersystem.entity.Order;
import dev.francode.ordersystem.entity.OrderDetails;
import dev.francode.ordersystem.entity.Product;
import dev.francode.ordersystem.entity.UserApp;
import dev.francode.ordersystem.entity.enums.EStatusOrder;
import dev.francode.ordersystem.exceptions.custom.ValidationException;
import dev.francode.ordersystem.mapper.OrderMapper;
import dev.francode.ordersystem.repository.OrderDetailsRepository;
import dev.francode.ordersystem.repository.OrderRepository;
import dev.francode.ordersystem.repository.ProductRepository;
import dev.francode.ordersystem.repository.UserRepository;
import dev.francode.ordersystem.service.interfaces.OrderService;
import dev.francode.ordersystem.service.spec.OrderSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, Long userId) {
        if (userId == null) {
            throw new ValidationException("Usuario no autenticado");
        }

        UserApp user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("Usuario no encontrado"));

        return createOrderInternal(orderRequest, user);
    }

    @Transactional
    public OrderResponse createOrderWithDefaultUser(OrderRequest orderRequest) {
        Long defaultUserId = 1L;
        UserApp user = userRepository.findById(defaultUserId)
                .orElseThrow(() -> new ValidationException("El usuario por defecto con ID 1 no existe"));
        return createOrderInternal(orderRequest, user);
    }

    private OrderResponse createOrderInternal(OrderRequest orderRequest, UserApp user) {
        List<OrderDetailsRequest> items = orderRequest.getProducts();

        if (items == null || items.isEmpty()) {
            throw new ValidationException("Debe agregar al menos un producto al pedido");
        }

        validateNoDuplicateProducts(items);

        Order order = new Order();
        order.setUser(user);
        order.setDate(LocalDateTime.now());
        order.setStatus(EStatusOrder.PENDIENTE);

        List<OrderDetails> orderProducts = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderDetailsRequest prodReq : items) {
            if (prodReq.getQuantity() == null || prodReq.getQuantity() <= 0) {
                throw new ValidationException("Cantidad inválida para producto ID " + prodReq.getProductId());
            }

            Product product = productRepository.findById(prodReq.getProductId())
                    .orElseThrow(() -> new ValidationException("Producto con ID " + prodReq.getProductId() + " no encontrado"));

            if (prodReq.getQuantity() > product.getStock()) {
                throw new ValidationException("Stock insuficiente para " + product.getName());
            }

            // Restar stock
            product.setStock(product.getStock() - prodReq.getQuantity());
            productRepository.save(product);

            // Usar precio con descuento si está presente, sino el precio normal
            BigDecimal effectivePrice = (product.getPriceDiscount() != null) ? product.getPriceDiscount() : product.getPrice();

            BigDecimal subtotal = effectivePrice.multiply(BigDecimal.valueOf(prodReq.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            OrderDetails op = new OrderDetails();
            op.setOrder(order);
            op.setProduct(product);
            op.setQuantity(prodReq.getQuantity());
            op.setPrice(effectivePrice);
            op.setSubTotal(subtotal);
            orderProducts.add(op);
        }

        order.setTotal(totalAmount);
        order.setOrderDetails(orderProducts);

        Order savedOrder = orderRepository.save(order);
        orderProductRepository.saveAll(orderProducts);

        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long orderId, OrderRequest orderRequest, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ValidationException("Pedido no encontrado"));

        if (!order.getUser().getId().equals(userId)) {
            throw new ValidationException("No puede modificar pedidos de otros usuarios");
        }

        if (order.getStatus() != EStatusOrder.PENDIENTE) {
            throw new ValidationException("Solo puede editar pedidos en estado PENDIENTE");
        }

        List<OrderDetailsRequest> items = orderRequest.getProducts();
        if (items == null || items.isEmpty()) {
            throw new ValidationException("El pedido debe contener al menos un producto");
        }

        validateNoDuplicateProducts(items);

        for (OrderDetails op : order.getOrderDetails()) {
            Product p = op.getProduct();
            p.setStock(p.getStock() + op.getQuantity());
            productRepository.save(p);
        }

        orderProductRepository.deleteAll(order.getOrderDetails());

        List<OrderDetails> updatedProducts = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderDetailsRequest prodReq : items) {
            Product product = productRepository.findById(prodReq.getProductId())
                    .orElseThrow(() -> new ValidationException("Producto con ID " + prodReq.getProductId() + " no encontrado"));

            if (prodReq.getQuantity() > product.getStock()) {
                throw new ValidationException("Stock insuficiente para " + product.getName());
            }

            product.setStock(product.getStock() - prodReq.getQuantity());
            productRepository.save(product);

            BigDecimal effectivePrice = (product.getPriceDiscount() != null) ? product.getPriceDiscount() : product.getPrice();
            BigDecimal subtotal = effectivePrice.multiply(BigDecimal.valueOf(prodReq.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            OrderDetails op = new OrderDetails();
            op.setOrder(order);
            op.setProduct(product);
            op.setQuantity(prodReq.getQuantity());
            op.setPrice(effectivePrice);
            op.setSubTotal(subtotal);
            updatedProducts.add(op);
        }

        order.setOrderDetails(updatedProducts);
        order.setTotal(totalAmount);

        Order savedOrder = orderRepository.save(order);
        orderProductRepository.saveAll(updatedProducts);

        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ValidationException("Pedido no encontrado"));

        UserApp user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("Usuario no encontrado"));

        if (!user.isAdmin() && !order.getUser().getId().equals(userId)) {
            throw new ValidationException("No puede acceder a pedidos de otros usuarios");
        }

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public Page<OrderResponse> getOrdersByUser(Long userId, OrderFilter filter, Pageable pageable) {
        if (!filter.isDateRangeValid()) {
            throw new ValidationException("Rango de fechas inválido");
        }
        Specification<Order> spec = OrderSpecifications.forUserFilter(userId, filter);
        return orderRepository.findAll(spec, pageable)
                .map(orderMapper::toOrderResponse);
    }

    @Override
    public Page<OrderResponse> getOrders(OrderAdminFilter filter, Pageable pageable) {
        if (!filter.isDateRangeValid()) {
            throw new ValidationException("Rango de fechas inválido");
        }
        Specification<Order> spec = OrderSpecifications.forAdminFilter(filter);
        return orderRepository.findAll(spec, pageable)
                .map(orderMapper::toOrderResponse);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ValidationException("Pedido no encontrado"));

        UserApp user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("Usuario no encontrado"));

        if (!order.getUser().getId().equals(userId) && !user.isAdmin()) {
            throw new ValidationException("No puede cancelar pedidos de otros usuarios");
        }

        if (order.getStatus() == EStatusOrder.CANCELADO) {
            throw new ValidationException("El pedido ya está cancelado");
        }

        if (!user.isAdmin() && order.getStatus() != EStatusOrder.PENDIENTE) {
            throw new ValidationException("Solo puede cancelar pedidos en estado PENDIENTE");
        }

        for (OrderDetails op : order.getOrderDetails()) {
            Product p = op.getProduct();
            p.setStock(p.getStock() + op.getQuantity());
            productRepository.save(p);
        }

        order.setStatus(EStatusOrder.CANCELADO);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ValidationException("Pedido no encontrado"));

        if (order.getStatus() == EStatusOrder.CANCELADO) {
            throw new ValidationException("No se puede confirmar un pedido cancelado");
        }

        if (order.getStatus() == EStatusOrder.CONFIRMADO) {
            throw new ValidationException("El pedido ya está confirmado");
        }

        order.setStatus(EStatusOrder.CONFIRMADO);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void sendOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ValidationException("Pedido no encontrado"));

        if (order.getStatus() == EStatusOrder.CANCELADO) {
            throw new ValidationException("No se puede enviar un pedido cancelado");
        }

        if (order.getStatus() == EStatusOrder.ENVIADO) {
            throw new ValidationException("El pedido ya está enviado");
        }

        order.setStatus(EStatusOrder.ENVIADO);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deliverOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ValidationException("Pedido no encontrado"));

        if (order.getStatus() == EStatusOrder.CANCELADO) {
            throw new ValidationException("No se puede entregar un pedido cancelado");
        }

        if (order.getStatus() == EStatusOrder.ENTREGADO) {
            throw new ValidationException("El pedido ya está entregado");
        }

        order.setStatus(EStatusOrder.ENTREGADO);
        orderRepository.save(order);
    }

    private void validateNoDuplicateProducts(List<OrderDetailsRequest> items) {
        Set<Long> productIds = new HashSet<>();
        for (OrderDetailsRequest item : items) {
            if (!productIds.add(item.getProductId())) {
                throw new ValidationException("No se puede repetir el producto con ID " + item.getProductId() + " en el pedido.");
            }
        }
    }

    @Override
    public OrderResponse getOrderByIdPublic(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ValidationException("Pedido no encontrado"));

        Long userIdAllowed = 1L; // el id del usuario autorizado

        if (!order.getUser().getId().equals(userIdAllowed)) {
            throw new ValidationException("No tiene acceso a este pedido");
        }

        return orderMapper.toOrderResponse(order);
    }


    @Override
    public Page<OrderResponse> getDefaultUserOrders(OrderFilter filter, Pageable pageable) {
        Long defaultUserId = 1L;

        if (!filter.isDateRangeValid()) {
            throw new ValidationException("Rango de fechas inválido");
        }

        Specification<Order> spec = OrderSpecifications.forUserFilter(defaultUserId, filter);

        return orderRepository.findAll(spec, pageable)
                .map(orderMapper::toOrderResponse);
    }

}