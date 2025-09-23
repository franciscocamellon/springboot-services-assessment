package br.edu.infnet.assessment.service;

import br.edu.infnet.assessment.config.exceptions.ConflictException;
import br.edu.infnet.assessment.config.jwt.JwtUtil;
import br.edu.infnet.assessment.config.utils.UserRoles;
import br.edu.infnet.assessment.dto.LoginResponse;
import br.edu.infnet.assessment.dto.RegisterResponse;
import br.edu.infnet.assessment.dto.TokenResponse;
import br.edu.infnet.assessment.dto.UserResponse;
import br.edu.infnet.assessment.entities.SystemUser;
import br.edu.infnet.assessment.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public TokenResponse login(LoginResponse loginResponse) {
        final String email = Objects.requireNonNull(loginResponse.email(), "email é obrigatório");
        final String senha = Objects.requireNonNull(loginResponse.password(), "password é obrigatória");

        SystemUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Credenciais inválidas."));

        if (!passwordEncoder.matches(senha, user.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        UserDetails principal = toSpringUserDetails(user);
        String token = jwtUtil.generateToken(principal);
        return new TokenResponse(token);
    }

    @Transactional
    public UserResponse register(RegisterResponse registerResponse) {
        final String name  = Objects.requireNonNull(registerResponse.name(),  "nome é obrigatório");
        final String email = Objects.requireNonNull(registerResponse.email(), "email é obrigatório");
        final String senha = Objects.requireNonNull(registerResponse.password(), "password é obrigatória");

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Já existe um usuário cadastrado com este email.");
        }

		SystemUser newUser = new SystemUser();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(senha));
        newUser.setActive(Boolean.TRUE);
        newUser.setRole(UserRoles.PROFESSOR);

		SystemUser savedUser = userRepository.save(newUser);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getActive(),
                savedUser.getRole().name()
        );
    }

    private UserDetails toSpringUserDetails(SystemUser systemUser) {
		var role = systemUser.getRole();
        Collection<SimpleGrantedAuthority> authorities = (role == null)
				? List.of()
				: List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));

        boolean disabled = systemUser.getActive() != null && !systemUser.getActive();

        return User.withUsername(systemUser.getEmail())
                .password(systemUser.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(disabled)
                .build();
    }

    public Collection<SystemUser> list() {
        return userRepository.findAll(Sort.by("nome"));
    }
}
