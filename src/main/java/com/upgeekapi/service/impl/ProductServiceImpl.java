package com.upgeekapi.service.impl;

import com.upgeekapi.dto.request.ProductRequestDTO;
import com.upgeekapi.dto.response.ProductDTO;
import com.upgeekapi.entity.Product;
import com.upgeekapi.exception.custom.DataConflictException;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import com.upgeekapi.mapper.ProductMapper;
import com.upgeekapi.repository.ProductRepository;
import com.upgeekapi.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Implementação da interface {@link ProductService}.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

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
    @Transactional
    public ProductDTO createProduct(ProductRequestDTO request) {
        productRepository.findByName(request.name()).ifPresent(p -> {
            throw new DataConflictException("Um produto com o nome '" + request.name() + "' já existe.");
        });

        Product newProduct = new Product(
                request.name(),
                request.description(),
                request.originalPrice(),
                request.xp(),
                request.imageUrl(),
                request.stockQuantity()
        );

        if (request.onSale() != null && request.onSale()) {
            newProduct.setOnSale(true);
            newProduct.setDiscountPrice(request.discountPrice());
        }

        Product savedProduct = productRepository.save(newProduct);
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long productId, ProductRequestDTO request) {
        Product productToUpdate = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID '" + productId + "' não encontrado para atualização."));

        productToUpdate.setName(request.name());
        productToUpdate.setDescription(request.description());
        productToUpdate.setOriginalPrice(request.originalPrice());
        productToUpdate.setXp(request.xp());
        productToUpdate.setImageUrl(request.imageUrl());
        productToUpdate.setStockQuantity(request.stockQuantity());
        productToUpdate.setOnSale(request.onSale() != null && request.onSale());
        productToUpdate.setDiscountPrice(request.onSale() ? request.discountPrice() : null);

        Product updatedProduct = productRepository.save(productToUpdate);
        return productMapper.toDto(updatedProduct);
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