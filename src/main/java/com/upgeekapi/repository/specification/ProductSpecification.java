package com.upgeekapi.repository.specification;

import com.upgeekapi.entity.Product;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> nameLike(String name) {
        if (name == null || name.isBlank()) return null;
        return (root, query, builder) -> builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> priceGreaterThanOrEqual(BigDecimal minPrice) {
        if (minPrice == null) return null;
        return (root, query, builder) -> {
            // Seleciona o preço ativo: se discountPrice não for nulo e onSale for true, usa ele; senão, usa originalPrice.
            Expression<BigDecimal> activePrice = builder.coalesce(root.get("discountPrice"), root.get("originalPrice"));
            return builder.greaterThanOrEqualTo(activePrice, minPrice);
        };
    }

    public static Specification<Product> priceLessThanOrEqual(BigDecimal maxPrice) {
        if (maxPrice == null) return null;
        return (root, query, builder) -> {
            Expression<BigDecimal> activePrice = builder.coalesce(root.get("discountPrice"), root.get("originalPrice"));
            return builder.lessThanOrEqualTo(activePrice, maxPrice);
        };
    }
}