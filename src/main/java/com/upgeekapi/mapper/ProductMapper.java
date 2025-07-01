package com.upgeekapi.mapper;

import com.upgeekapi.dto.response.ProductDTO;
import com.upgeekapi.entity.Product;
import org.springframework.stereotype.Component;

/**
 * Implementação do {@link Mapper} para converter a entidade {@link Product} em um {@link ProductDTO}.
 */
@Component
public class ProductMapper implements Mapper<Product, ProductDTO> {
    @Override
    public ProductDTO toDto(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getOriginalPrice(),
                product.getDiscountPrice(),
                product.isOnSale(),
                product.getXp(),
                product.getImageUrl()
        );
    }
}