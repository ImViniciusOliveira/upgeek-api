-- Limpar e popular o banco com dados de desenvolvimento.
-- Por ser uma migração REPETÍVEL (R__), o Flyway a executará sempre que seu conteúdo for alterado,
-- garantindo um ambiente de desenvolvimento sempre limpo e previsível.

-- 1. LIMPEZA COMPLETA E EFICIENTE DAS TABELAS
-- O TRUNCATE apaga todos os dados e o CASCADE cuida das dependências.
TRUNCATE TABLE users, products CASCADE;

-- 2. RESET DAS SEQUÊNCIAS DE ID (PASSO CRÍTICO)
-- O TRUNCATE não reseta os contadores de ID. Fazemos isso manualmente para garantir
-- que as novas inserções comecem sempre do ID 1, tornando o script previsível.
ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE products_id_seq RESTART WITH 1;

-- 3. INSERÇÃO DE DADOS DE DESENVOLVIMENTO
-- Agora que as tabelas estão vazias e os contadores resetados, podemos inserir os dados.

-- ETAPA A: INSERIR OS USUÁRIOS (Agora eles receberão IDs de 1 a 10)
INSERT INTO users (username, email, name, cpf, password) VALUES
                                                             ('kain_admin', 'kain.admin@upgeek.com', 'Kain (Admin)', '87543940057', '$2a$12$Sg3d8s8F4s77zWeeNTbFVero5KwOWSik21QdJDvK3Hk25r.OKt99e'), -- Senha: AdminLegacy#7890
                                                             ('tenebris_prime', 'imperador@tenebris.net', 'Imperador Tenebris', '52998224725', '$2a$12$fRwg5oHljRK4hW/4ZIj0m.qX1bN9Qe8dU2dqACTMjgNgFebqKAyVe'), -- Senha: OrdemAbsoluta#2025
                                                             ('lira_system', 'lira.sys@scarlate.org', 'Lira Valen System', '28745563895', '$2a$10$1a2b3c4d5e6f7g8h9i0j.kL.mN.oP.qR.sT.uV.wX.y'), -- Senha: ScarlateControl#123
                                                             ('jax_operator', 'jax.op@duum.net', 'Jax Operator', '78433510050', '$2a$10$zYxWvUtSrQpOnMlKjIhGfEdCbA.1.2.3.4.5.6.7.8.9'), -- Senha: OperatorPass#3210
                                                             ('nyx_shadow', 'nyx.shadow@tenebris.net', 'Nyx Shadow Ops', '88828547031', '$2a$10$pOnMlKjIhGfEdCbA.zYxWvUtSrQ.1.2.3.4.5.6.7.8.9'), -- Senha: ShadowKey#456789
                                                             ('Kain Renegade', 'kain.renegade@duum.net', 'Kain', '21558440031', '$2a$12$8Fd6DyhVarTxkWl815pV2.SEkM0.wWLF4ZUkiUqkuArqsV6fP4mVO'), -- Senha: darkLight#123456
                                                             ('Jax Scout', 'jax.scout@duum.net', 'Jax', '32139122036', '$2a$10$qWeRtYuIoPaSdFgHjKlZxCvBnM.1.2.3.4.5.6.7.8.9.0'), -- Senha: ScoutPass#12345
                                                             ('Lira Valen', 'lira.valen@scarlate.org', 'Lira Valen', '65432198700', '$2a$10$mNbVcXzLkJhGfDsApOiUyTrEwQ.1.2.3.4.5.6.7.8.9'), -- Senha: AliancaScarlate#123
                                                             ('Echo Tech', 'echo.tech@scarlate.org', 'Echo', '98765432109', '$2a$10$lKjHgFdSaPoIuYtReWq.1.2.3.4.5.6.7.8.9.0.a.b.c'), -- Senha: TechieDream#8888
                                                             ('Silas Merc', 'silas.merc@duum.net', 'Silas', '12345678909', '$2a$10$zXcVbNmAsDfGjKl.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e'); -- Senha: MercenaryLife#999

-- ETAPA B: ASSOCIAR OS PAPÉIS AOS USUÁRIOS (Agora os IDs 1-10 existem e correspondem)
INSERT INTO user_roles (user_id, role) VALUES
                                           (1, 'ROLE_USER'), (1, 'ROLE_ADMIN'),
                                           (2, 'ROLE_USER'), (2, 'ROLE_ADMIN'),
                                           (3, 'ROLE_USER'), (3, 'ROLE_ADMIN'),
                                           (4, 'ROLE_USER'), (4, 'ROLE_ADMIN'),
                                           (5, 'ROLE_USER'), (5, 'ROLE_ADMIN'),
                                           (6, 'ROLE_USER'),
                                           (7, 'ROLE_USER'),
                                           (8, 'ROLE_USER'),
                                           (9, 'ROLE_USER'),
                                           (10, 'ROLE_USER');

-- ETAPA C: INSERIR OS PRODUTOS
INSERT INTO products (name, description, original_price, xp, image_url, stock_quantity, on_sale, discount_price) VALUES
                                                                                                                     ('Imperador Tenebris - Edição Arconte', 'Peça central do Império.', 499.90, 1500, '/assets/images/tenebris.webp', 10, true, 399.90),
                                                                                                                     ('Caça Stealth da Aliança', 'O ápice da tecnologia Scarlate.', 799.90, 2500, '/assets/images/alianca-fighter.webp', 5, false, null),
                                                                                                                     ('Armadura de Renegado de Kain', 'Tecnologia híbrida, um símbolo de rebelião.', 1499.90, 5000, '/assets/images/kain-armor.webp', 2, false, null),
                                                                                                                     ('Rifle de Pulso Scarlate', 'Arma padrão das forças da Aliança.', 350.00, 750, '/assets/images/scarlate-rifle.webp', 50, false, null),
                                                                                                                     ('Drone Sentinela de Tenebris', 'Miniatura funcional do drone de vigilância do império.', 250.00, 500, '/assets/images/sentinel-drone.webp', 25, false, null),
                                                                                                                     ('Holomapa de Rubrum', 'Projetor holográfico da cidade oculta da Aliança.', 180.00, 300, '/assets/images/rubrum-map.webp', 15, false, null),
                                                                                                                     ('Adaga Cerimonial de Tenebris', 'Réplica da adaga usada nas cerimônias do Império.', 299.90, 600, '/assets/images/tenebris-dagger.webp', 20, true, 249.90),
                                                                                                                     ('Kit de Camuflagem de Kain', 'Tecido com nanotecnologia para invisibilidade temporária.', 450.00, 1200, '/assets/images/kain-cloak.webp', 8, false, null),
                                                                                                                     ('Medalha de Honra Scarlate', 'Condecoração concedida a heróis da Aliança.', 99.90, 200, '/assets/images/scarlate-medal.webp', 100, false, null),
                                                                                                                     ('Trono de Obsidiana em Miniatura', 'Réplica do trono do Imperador Tenebris.', 899.90, 3000, '/assets/images/obsidian-throne.webp', 4, false, null);

-- ETAPA D: ASSOCIAR AS TAGS AOS PRODUTOS
INSERT INTO product_tags (product_id, tag) VALUES
                                               (1, 'imperio-tenebris'), (1, 'edicao-arconte'), (1, 'destaques'),
                                               (2, 'alianca-scarlate'), (2, 'tecnologia-stealth'),
                                               (3, 'renegado'), (3, 'tecnologia-stealth'), (3, 'destaques'),
                                               (4, 'alianca-scarlate'),
                                               (5, 'imperio-tenebris'),
                                               (6, 'alianca-scarlate'), (6, 'colecionavel'),
                                               (7, 'imperio-tenebris'), (7, 'colecionavel'),
                                               (8, 'renegado'), (8, 'tecnologia-stealth'),
                                               (9, 'alianca-scarlate'), (9, 'colecionavel'),
                                               (10, 'imperio-tenebris'), (10, 'edicao-arconte');