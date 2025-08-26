package dev.francode.ordersystem.service.spec;

import dev.francode.ordersystem.dto.order.OrderFilter;
import dev.francode.ordersystem.dto.order.OrderAdminFilter;
import dev.francode.ordersystem.entity.Order;
import dev.francode.ordersystem.entity.enums.EStatusOrder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class OrderSpecifications {

    public static Specification<Order> forUserFilter(Long userId, OrderFilter filter) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            predicates.add(cb.equal(root.get("user").get("id"), userId));

            if (filter.getStatus() != null) {
                try {
                    EStatusOrder status = EStatusOrder.valueOf(filter.getStatus().toUpperCase());
                    predicates.add(cb.equal(root.get("status"), status));
                } catch (IllegalArgumentException e) {
                    // Invalid status string => no results
                    predicates.add(cb.disjunction());
                }
            }
            if (filter.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), filter.getStartDate()));
            }
            if (filter.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), filter.getEndDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Order> forAdminFilter(OrderAdminFilter filter) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();

            if (filter.getUserId() != null) {
                predicates.add(cb.equal(root.get("user").get("id"), filter.getUserId()));
            }
            if (filter.getStatus() != null) {
                try {
                    EStatusOrder status = EStatusOrder.valueOf(filter.getStatus().toUpperCase());
                    predicates.add(cb.equal(root.get("status"), status));
                } catch (IllegalArgumentException e) {
                    predicates.add(cb.disjunction());
                }
            }
            if (filter.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), filter.getStartDate()));
            }
            if (filter.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), filter.getEndDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
