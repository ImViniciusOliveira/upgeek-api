package com.upgeekapi.controller;

import com.upgeekapi.controller.assembler.ProductHateoasAssembler;
import com.upgeekapi.dto.hateoas.ProductHateoasDTO;
import com.upgeekapi.dto.request.ProductRequestDTO;
import com.upgeekapi.dto.request.ProductSearchRequestDto;
import com.upgeekapi.dto.response.ErrorDTO;
import com.upgeekapi.entity.Product;
import com.upgeekapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**
     * Retorna uma coleção de todos os produtos.
     * @return Um {@link CollectionModel} contendo os produtos com links HATEOAS.
     */
    @GetMapping
    @Operation(summary = "Listar todos os produtos", description = "Retorna uma coleção paginada de todos os produtos disponíveis no catálogo.")
    public CollectionModel<ProductHateoasDTO> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return assembler.toCollectionModel(products);
    }

    /**
     * Busca um produto específico pelo seu ID.
     * @param id O ID único do produto.
     * @return O {@link ProductHateoasDTO} correspondente ao produto encontrado.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar um produto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ProductHateoasDTO getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return assembler.toModel(product);
    }

    /**
     * Retorna uma coleção de produtos que estão atualmente em promoção.
     * @return Um {@link CollectionModel} contendo os produtos em promoção.
     */
    @GetMapping("/on-sale")
    @Operation(summary = "Listar produtos em promoção")
    public CollectionModel<ProductHateoasDTO> getProductsOnSale() {
        List<Product> products = productService.getProductsOnSale();
        return assembler.toCollectionModel(products);
    }

    /**
     * Retorna uma coleção de produtos associados a uma tag específica.
     * @param tag A tag a ser buscada.
     * @return Um {@link CollectionModel} contendo os produtos com a tag especificada.
     */
    @GetMapping("/tag/{tag}")
    @Operation(summary = "Listar produtos por uma tag específica")
    public CollectionModel<ProductHateoasDTO> getProductsByTag(@PathVariable String tag) {
        List<Product> products = productService.getProductsByTag(tag);
        return assembler.toCollectionModel(products);
    }

    /**
     * Busca produtos com base em um conjunto de filtros dinâmicos encapsulados em um DTO.
     * <p>
     * Este endpoint utiliza o método POST para permitir um corpo de requisição (payload)
     * estruturado, o que é ideal para critérios de busca complexos. O DTO recebido
     * é validado antes do processamento, garantindo a integridade dos filtros.
     *
     * @param searchDto DTO contendo os critérios de busca validados (nome, faixa de preço, etc.).
     * @return Um {@link CollectionModel} com os produtos que correspondem aos critérios.
     */
    @PostMapping("/search")
    @Operation(summary = "Buscar produtos com filtros dinâmicos", description = "Utiliza POST para permitir um corpo de requisição com múltiplos critérios de busca.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Critérios de busca inválidos.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado com os critérios informados.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public CollectionModel<ProductHateoasDTO> searchProducts(
            @Valid @RequestBody ProductSearchRequestDto searchDto
    ) {
        List<Product> products = productService.searchProducts(searchDto);
        return assembler.toCollectionModel(products);
    }

    /**
     * Cria um novo produto no sistema. Requer permissão de administrador.
     * @param request DTO com os dados validados do novo produto.
     * @return Uma resposta HTTP 201 (Created) com a localização do novo recurso e o DTO do produto criado no corpo.
     */
    @PostMapping
    @Operation(summary = "Criar um novo produto (Admin)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                    headers = @Header(name = "Location", description = "URL do novo recurso", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Requer permissão de administrador.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<ProductHateoasDTO> createProduct(@Valid @RequestBody ProductRequestDTO request) {
        Product createdProduct = productService.createProduct(request);
        ProductHateoasDTO model = assembler.toModel(createdProduct);

        // Constrói a URI do novo recurso para o cabeçalho 'Location', seguindo as boas práticas REST.
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.getId())
                .toUri();

        return ResponseEntity.created(location).body(model);
    }

    /**
     * Atualiza um produto existente. Requer permissão de administrador.
     * @param id O ID do produto a ser atualizado.
     * @param request DTO com os novos dados do produto.
     * @return Uma resposta HTTP 200 (OK) com o DTO do produto atualizado no corpo.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um produto existente (Admin)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Requer permissão de administrador.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<ProductHateoasDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO request) {
        Product updatedProduct = productService.updateProduct(id, request);
        ProductHateoasDTO model = assembler.toModel(updatedProduct);
        return ResponseEntity.ok(model);
    }

    /**
     * Deleta um produto do sistema. Requer permissão de administrador.
     * <p>
     * O retorno {@code ResponseEntity<Void>} é crucial para que o Spring HATEOAS consiga
     * construir links para este método sem causar o erro 'linkTo(void)', uma vez que
     * a operação de delete não retorna um corpo.
     *
     * @param id O ID do produto a ser deletado.
     * @return Uma resposta HTTP 204 (No Content) indicando sucesso.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um produto (Admin)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Requer permissão de administrador.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}