package com.upgeekapi.controller;

import com.upgeekapi.dto.request.ProductRequestDTO;
import com.upgeekapi.dto.response.ProductDTO;
import com.upgeekapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Endpoints para gerenciamento de produtos e catálogo")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os produtos", description = "Retorna uma lista de todos os produtos disponíveis na loja.")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/on-sale")
    @Operation(summary = "Listar produtos em promoção", description = "Retorna uma lista de todos os produtos atualmente com desconto.")
    public ResponseEntity<List<ProductDTO>> getProductsOnSale() {
        return ResponseEntity.ok(productService.getProductsOnSale());
    }

    @GetMapping("/tag/{tag}")
    @Operation(summary = "Listar produtos por uma tag/coleção específica")
    public ResponseEntity<List<ProductDTO>> getProductsByTag(@PathVariable String tag) {
        return ResponseEntity.ok(productService.getProductsByTag(tag));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar produtos com filtros dinâmicos")
    public ResponseEntity<List<ProductDTO>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        List<ProductDTO> products = productService.searchProducts(name, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @Operation(summary = "Criar um novo produto (Admin)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductRequestDTO request) {
        ProductDTO createdProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um produto existente (Admin)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO request) {
        ProductDTO updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um produto (Admin)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}