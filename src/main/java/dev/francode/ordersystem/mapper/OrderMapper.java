package dev.francode.ordersystem.mapper;

import dev.francode.ordersystem.dto.order.*;
import dev.francode.ordersystem.entity.Order;
import dev.francode.ordersystem.entity.OrderDetails;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToString")
    OrderResponse toOrderResponse(Order order);

    @Mapping(source = "product.name", target = "productName")
    OrderDetailsResponse toOrderDetailsResponse(OrderDetails orderProduct);

    @Named("statusToString")
    default String statusToString(Enum<?> status) {
        return status == null ? null : status.name();
    }
}