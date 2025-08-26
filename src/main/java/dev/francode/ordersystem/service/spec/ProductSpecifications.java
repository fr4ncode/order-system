package dev.francode.ordersystem.service.spec;

import dev.francode.ordersystem.dto.product.ProductFilter;
import dev.francode.ordersystem.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class ProductSpecifications {

    public static Specification<Product> filterBy(ProductFilter filter) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();

            if (filter.getSearch() != null && !filter.getSearch().trim().isEmpty()) {
                predicates.add(
                        cb.like(cb.lower(root.get("name")), "%" + filter.getSearch().trim().toLowerCase() + "%")
                );
            }

            if (filter.getMinprice() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(root.get("price"), filter.getMinprice())
                );
            }

            if (filter.getMaxprice() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(root.get("price"), filter.getMaxprice())
                );
            }

            if (filter.getCategory() != null) {
                predicates.add(
                        cb.equal(root.get("category").get("id"), filter.getCategory())
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
