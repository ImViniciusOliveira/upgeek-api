package com.upgeekapi.config;

import com.upgeekapi.entity.Product;
import com.upgeekapi.entity.Role;
import com.upgeekapi.entity.User;
import com.upgeekapi.repository.ProductRepository;
import com.upgeekapi.repository.RoleRepository;
import com.upgeekapi.repository.UserRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
@Profile("dev")
public class DataInitializer {

    // Usando records privados para agrupar dados, limpando as assinaturas dos métodos.
    @Builder private record UserData(String username, String email, String name, String cpf, String password, Set<Role> roles) {}
    @Builder private record ProductData(String name, String description, BigDecimal originalPrice, Long xp, String imageUrl, int stockQuantity, boolean onSale, BigDecimal discountPrice, Set<String> tags) {}

    @Bean
    @Transactional
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            RoleRepository roleRepository,
            ProductRepository productRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            log.info("### INICIANDO POPULAÇÃO COMPLETA DO BANCO DE DADOS (PERFIL DEV) ###");

            userRepository.deleteAllInBatch();
            roleRepository.deleteAllInBatch();
            productRepository.deleteAllInBatch();
            log.info(">>> Dados antigos removidos com sucesso.");

            // 1. Cria as Roles essenciais
            Role userRole = createRoleIfNotExists(roleRepository, "ROLE_USER");
            Role adminRole = createRoleIfNotExists(roleRepository, "ROLE_ADMIN");

            // 2. Define e cria todos os usuários
            var usersToCreate = List.of(
                    // Admins
                    UserData.builder().username("kain_admin").email("kain.admin@upgeek.com").name("Kain (Admin)").cpf("87543940057").password("AdminLegacy#7890").roles(Set.of(userRole, adminRole)).build(),
                    UserData.builder().username("tenebris_prime").email("imperador@tenebris.net").name("Imperador Tenebris").cpf("52998224725").password("OrdemAbsoluta#2025").roles(Set.of(userRole, adminRole)).build(),
                    UserData.builder().username("lira_system").email("lira.sys@scarlate.org").name("Lira Valen System").cpf("28745563895").password("ScarlateControl#123").roles(Set.of(userRole, adminRole)).build(),
                    UserData.builder().username("jax_operator").email("jax.op@duum.net").name("Jax Operator").cpf("78433510050").password("OperatorPass#3210").roles(Set.of(userRole, adminRole)).build(),
                    UserData.builder().username("nyx_shadow").email("nyx.shadow@tenebris.net").name("Nyx Shadow Ops").cpf("88828547031").password("ShadowKey#456789").roles(Set.of(userRole, adminRole)).build(),
                    // Usuários Normais
                    UserData.builder().username("Kain Renegade").email("kain.renegade@duum.net").name("Kain").cpf("21558440031").password("darkLight#123456").roles(Set.of(userRole)).build(),
                    UserData.builder().username("Jax Scout").email("jax.scout@duum.net").name("Jax").cpf("32139122036").password("ScoutPass#12345").roles(Set.of(userRole)).build(),
                    UserData.builder().username("Lira Valen").email("lira.valen@scarlate.org").name("Lira Valen").cpf("65432198700").password("AliancaScarlate#123").roles(Set.of(userRole)).build(),
                    UserData.builder().username("Echo Tech").email("echo.tech@scarlate.org").name("Echo").cpf("98765432109").password("TechieDream#8888").roles(Set.of(userRole)).build(),
                    UserData.builder().username("Silas Merc").email("silas.merc@duum.net").name("Silas").cpf("12345678909").password("MercenaryLife#999").roles(Set.of(userRole)).build()
            );
            usersToCreate.forEach(userData -> createUser(userRepository, passwordEncoder, userData));

            // 3. Define e cria todos os produtos
            var productsToCreate = List.of(
                    ProductData.builder().name("Imperador Tenebris - Edição Arconte").description("Peça central do Império.").originalPrice(new BigDecimal("499.90")).xp(1500L).imageUrl("/assets/images/tenebris.webp").stockQuantity(10).onSale(true).discountPrice(new BigDecimal("399.90")).tags(Set.of("imperio-tenebris", "edicao-arconte", "destaques")).build(),
                    ProductData.builder().name("Caça Stealth da Aliança").description("O ápice da tecnologia Scarlate.").originalPrice(new BigDecimal("799.90")).xp(2500L).imageUrl("/assets/images/alianca-fighter.webp").stockQuantity(5).tags(Set.of("alianca-scarlate", "tecnologia-stealth")).build(),
                    ProductData.builder().name("Armadura de Renegado de Kain").description("Tecnologia híbrida, um símbolo de rebelião.").originalPrice(new BigDecimal("1499.90")).xp(5000L).imageUrl("/assets/images/kain-armor.webp").stockQuantity(2).tags(Set.of("renegado", "tecnologia-stealth", "destaques")).build(),
                    ProductData.builder().name("Rifle de Pulso Scarlate").description("Arma padrão das forças da Aliança.").originalPrice(new BigDecimal("350.00")).xp(750L).imageUrl("/assets/images/scarlate-rifle.webp").stockQuantity(50).tags(Set.of("alianca-scarlate")).build(),
                    ProductData.builder().name("Drone Sentinela de Tenebris").description("Miniatura funcional do drone de vigilância do império.").originalPrice(new BigDecimal("250.00")).xp(500L).imageUrl("/assets/images/sentinel-drone.webp").stockQuantity(25).tags(Set.of("imperio-tenebris")).build(),
                    ProductData.builder().name("Holomapa de Rubrum").description("Projetor holográfico da cidade oculta da Aliança.").originalPrice(new BigDecimal("180.00")).xp(300L).imageUrl("/assets/images/rubrum-map.webp").stockQuantity(15).tags(Set.of("alianca-scarlate", "colecionavel")).build(),
                    ProductData.builder().name("Adaga Cerimonial de Tenebris").description("Réplica da adaga usada nas cerimônias do Império.").originalPrice(new BigDecimal("299.90")).xp(600L).imageUrl("/assets/images/tenebris-dagger.webp").stockQuantity(20).onSale(true).discountPrice(new BigDecimal("249.90")).tags(Set.of("imperio-tenebris", "colecionavel")).build(),
                    ProductData.builder().name("Kit de Camuflagem de Kain").description("Tecido com nanotecnologia para invisibilidade temporária.").originalPrice(new BigDecimal("450.00")).xp(1200L).imageUrl("/assets/images/kain-cloak.webp").stockQuantity(8).tags(Set.of("renegado", "tecnologia-stealth")).build(),
                    ProductData.builder().name("Medalha de Honra Scarlate").description("Condecoração concedida a heróis da Aliança.").originalPrice(new BigDecimal("99.90")).xp(200L).imageUrl("/assets/images/scarlate-medal.webp").stockQuantity(100).tags(Set.of("alianca-scarlate", "colecionavel")).build(),
                    ProductData.builder().name("Trono de Obsidiana em Miniatura").description("Réplica do trono do Imperador Tenebris.").originalPrice(new BigDecimal("899.90")).xp(3000L).imageUrl("/assets/images/obsidian-throne.webp").stockQuantity(4).tags(Set.of("imperio-tenebris", "edicao-arconte")).build()
            );
            productsToCreate.forEach(productData -> createProduct(productRepository, productData));

            log.info("### POPULAÇÃO DO BANCO DE DADOS CONCLUÍDA! ###");
        };
    }

    private Role createRoleIfNotExists(RoleRepository repo, String name) {
        return repo.findByName(name).orElseGet(() -> {
            log.info(">>> Criando role padrão: {}", name);
            return repo.save(Role.builder().name(name).build());
        });
    }

    private void createUser(UserRepository repo, PasswordEncoder encoder, UserData data) {
        if (repo.findByEmail(data.email()).isEmpty()) {
            User user = User.builder()
                    .username(data.username())
                    .email(data.email())
                    .name(data.name())
                    .cpf(data.cpf())
                    .password(encoder.encode(data.password()))
                    .roles(data.roles())
                    .build();
            repo.save(user);
            log.info(">>> Usuário '{}' criado.", data.name());
        }
    }

    private void createProduct(ProductRepository repo, ProductData data) {
        if (repo.findByName(data.name()).isEmpty()) {
            Product product = Product.builder()
                    .name(data.name())
                    .description(data.description())
                    .originalPrice(data.originalPrice())
                    .xp(data.xp())
                    .imageUrl(data.imageUrl())
                    .stockQuantity(data.stockQuantity())
                    .onSale(data.onSale())
                    .discountPrice(data.onSale() ? data.discountPrice() : null)
                    .tags(data.tags())
                    .build();
            repo.save(product);
            log.info(">>> Produto '{}' criado.", data.name());
        }
    }
}