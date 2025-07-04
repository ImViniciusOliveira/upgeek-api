package com.upgeekapi.service.impl;

import com.upgeekapi.dto.request.ProductRequestDTO;
import com.upgeekapi.dto.response.ProductDTO;
import com.upgeekapi.entity.Product;
import com.upgeekapi.exception.custom.DataConflictException;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import com.upgeekapi.mapper.ProductMapper;
import com.upgeekapi.repository.ProductRepository;
import com.upgeekapi.repository.specification.ProductSpecification;
import com.upgeekapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementação da interface {@link ProductService}.
 * Contém a lógica de negócio para o gerenciamento completo de produtos.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productMapper.toDto(productRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsOnSale() {
        return productMapper.toDto(productRepository.findByOnSaleTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByTag(String tag) {
        return productMapper.toDto(productRepository.findByTagsContaining(tag));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String name, BigDecimal minPrice, BigDecimal maxPrice) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (name != null && !name.isBlank()) {
            spec = spec.and(ProductSpecification.nameLike(name));
        }
        if (minPrice != null) {
            spec = spec.and(ProductSpecification.priceGreaterThanOrEqual(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(ProductSpecification.priceLessThanOrEqual(maxPrice));
        }

        return productMapper.toDto(productRepository.findAll(spec));
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductRequestDTO request) {
        productRepository.findByName(request.name()).ifPresent(p -> {
            throw new DataConflictException("Um produto com o nome '" + request.name() + "' já existe.");
        });

        boolean isOnSale = request.onSale() != null && request.onSale();

        Product newProduct = Product.builder()
                .name(request.name())
                .description(request.description())
                .originalPrice(request.originalPrice())
                .xp(request.xp())
                .imageUrl(request.imageUrl())
                .stockQuantity(request.stockQuantity())
                .tags(request.tags() != null ? request.tags() : java.util.Collections.emptySet())
                .onSale(isOnSale)
                .discountPrice(isOnSale ? request.discountPrice() : null)
                .build();

        Product savedProduct = productRepository.save(newProduct);
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long productId, ProductRequestDTO request) {
        Product productToUpdate = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID '" + productId + "' não encontrado para atualização."));

        productRepository.findByName(request.name())
                .filter(foundProduct -> !foundProduct.getId().equals(productId))
                .ifPresent(p -> {
                    throw new DataConflictException("O nome '" + request.name() + "' já está em uso por outro produto.");
                });

        productToUpdate.setName(request.name());
        productToUpdate.setDescription(request.description());
        productToUpdate.setOriginalPrice(request.originalPrice());
        productToUpdate.setXp(request.xp());
        productToUpdate.setImageUrl(request.imageUrl());
        productToUpdate.setStockQuantity(request.stockQuantity());
        productToUpdate.setTags(request.tags());

        boolean isOnSale = request.onSale() != null && request.onSale();
        productToUpdate.setOnSale(isOnSale);
        productToUpdate.setDiscountPrice(isOnSale ? request.discountPrice() : null);

        Product savedProduct = productRepository.save(productToUpdate);

        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Não é possível deletar. Produto com ID '" + productId + "' não encontrado.");
        }
        productRepository.deleteById(productId);
    }
}