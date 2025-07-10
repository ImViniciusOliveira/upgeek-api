package com.upgeekapi.controller;

import com.upgeekapi.controller.assembler.ProductHateoasAssembler;
import com.upgeekapi.dto.hateoas.ProductHateoasDTO;
import com.upgeekapi.dto.request.ProductRequestDTO;
import com.upgeekapi.entity.Product;
import com.upgeekapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

/**
 * Controller REST para o gerenciamento de produtos.
 * <p>
 * Atua como um orquestrador, recebendo requisições HTTP, delegando a lógica de negócio
 * para o {@link ProductService} e usando o {@link ProductHateoasAssembler} para
 * construir as respostas HATEOAS.
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Endpoints para gerenciamento de produtos e catálogo")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductHateoasAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar todos os produtos")
    public CollectionModel<ProductHateoasDTO> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return assembler.toCollectionModel(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um produto por ID")
    public ProductHateoasDTO getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return assembler.toModel(product);
    }

    @GetMapping("/on-sale")
    @Operation(summary = "Listar produtos em promoção")
    public CollectionModel<ProductHateoasDTO> getProductsOnSale() {
        List<Product> products = productService.getProductsOnSale();
        return assembler.toCollectionModel(products);
    }

    @GetMapping("/tag/{tag}")
    @Operation(summary = "Listar produtos por uma tag específica")
    public CollectionModel<ProductHateoasDTO> getProductsByTag(@PathVariable String tag) {
        List<Product> products = productService.getProductsByTag(tag);
        return assembler.toCollectionModel(products);
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar produtos com filtros dinâmicos")
    public CollectionModel<ProductHateoasDTO> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        List<Product> products = productService.searchProducts(name, minPrice, maxPrice);
        return assembler.toCollectionModel(products);
    }

    @PostMapping
    @Operation(summary = "Criar um novo produto (Admin)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
            headers = @Header(name = "Location", description = "URL do novo recurso",
                    schema = @Schema(type = "string")))
    public ResponseEntity<ProductHateoasDTO> createProduct(@Valid @RequestBody ProductRequestDTO request) {
        Product createdProduct = productService.createProduct(request);
        ProductHateoasDTO model = assembler.toModel(createdProduct);

        // Constrói a URI do novo recurso para o cabeçalho 'Location'.
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.getId())
                .toUri();

        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um produto existente (Admin)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ProductHateoasDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO request) {
        Product updatedProduct = productService.updateProduct(id, request);
        ProductHateoasDTO model = assembler.toModel(updatedProduct);
        return ResponseEntity.ok(model);
    }

    /**
     * O retorno {@code ResponseEntity<Void>} é crucial para que o Spring HATEOAS consiga
     * construir links para este método sem causar o erro 'linkTo(void)'.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um produto (Admin)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}