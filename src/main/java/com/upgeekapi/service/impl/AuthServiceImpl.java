package com.upgeekapi.service.impl;

import com.upgeekapi.core.security.AuthPrincipal;
import com.upgeekapi.dto.request.LoginRequestDTO;
import com.upgeekapi.dto.request.RegistrationRequestDTO;
import com.upgeekapi.dto.response.LoginDTO;
// RoleEnum não é mais necessário aqui
import com.upgeekapi.entity.User;
import com.upgeekapi.exception.custom.AuthenticationException;
import com.upgeekapi.repository.UserRepository;
import com.upgeekapi.service.AuthService;
import com.upgeekapi.service.TokenService;
import com.upgeekapi.service.validation.UserUniquenessValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Set não é mais necessário aqui
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * Implementação da interface {@link AuthService}.
 * <p>
 * Orquestra a lógica de negócio para registro e autenticação de usuários,
 * coordenando com o repositório, o serviço de token e os validadores.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserUniquenessValidator uniquenessValidator;

    // --- O método login() permanece o mesmo, pois já está excelente ---
    @Override
    public LoginDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthenticationException("Credenciais inválidas."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthenticationException("Credenciais inválidas.");
        }

        AuthPrincipal principal = AuthPrincipal.from(user);
        String token = tokenService.generateToken(principal);

        return new LoginDTO(token);
    }

    /**
     * {@inheritDoc}
     * <p>
     * O processo de registro é transacional e segue os seguintes passos:
     * <ol>
     *     <li>Valida a unicidade dos dados (email, CPF).</li>
     *     <li>Gera um nome de usuário único.</li>
     *     <li>Delega a criação da entidade {@link User} para a própria entidade.</li>
     *     <li>Persiste o novo usuário no banco de dados.</li>
     * </ol>
     */
    @Override
    @Transactional
    public void register(RegistrationRequestDTO request) {
        // 1. Valida conflitos de dados.
        uniquenessValidator.validate(request);

        // 2. Gera um username único.
        String uniqueUsername = generateUniqueUsername(request.email());

        // 3. Usa o método de fábrica da entidade para criar o novo usuário.
        User newUser = User.from(request, passwordEncoder, uniqueUsername);

        // 4. Salva o novo usuário.
        userRepository.save(newUser);
    }

    /**
     * Gera um username único a partir de um email de forma funcional e declarativa.
     * <p>
     * O método primeiro sanitiza a parte local do email para criar um nome base.
     * Em seguida, ele usa um {@link Stream} para verificar a disponibilidade.
     * Se o nome base já estiver em uso, ele gera novos nomes com sufixos
     * numéricos aleatórios até encontrar um que não exista no banco.
     *
     * @param email O email a partir do qual o username será gerado.
     * @return Uma string de username garantidamente única.
     */
    private String generateUniqueUsername(String email) {
        String baseUsername = email.split("@")[0]
                .replaceAll("[^a-zA-Z0-9_.-]", "")
                .toLowerCase();

        if (baseUsername.isBlank() || baseUsername.length() < 3) {
            baseUsername = "user";
        }

        final String finalBaseUsername = baseUsername;

        // Adicionado um .limit(100) como uma salvaguarda contra loops infinitos em cenários extremos.
        return Stream.iterate(finalBaseUsername, username -> finalBaseUsername + ThreadLocalRandom.current().nextInt(10000, 100000))
                .limit(100) // Medida de segurança para evitar um loop infinito improvável.
                .filter(username -> userRepository.findByUsername(username).isEmpty())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Não foi possível gerar um nome de usuário único após 100 tentativas."));
    }
}