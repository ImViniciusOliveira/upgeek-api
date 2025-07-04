package com.upgeekapi.service;

import com.upgeekapi.dto.request.ProductRequestDTO;
import com.upgeekapi.dto.response.ProductDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface que define o contrato para os serviços relacionados a produtos.
 */
public interface ProductService {
    List<ProductDTO> getAllProducts();
    List<ProductDTO> getProductsOnSale();
    ProductDTO createProduct(ProductRequestDTO request);
    ProductDTO updateProduct(Long productId, ProductRequestDTO request);
    void deleteProduct(Long productId);

    List<ProductDTO> searchProducts(String name, BigDecimal minPrice, BigDecimal maxPrice);
    List<ProductDTO> getProductsByTag(String tag);
}