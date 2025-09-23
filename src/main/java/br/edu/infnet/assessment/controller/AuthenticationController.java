package br.edu.infnet.assessment.controller;

import br.edu.infnet.assessment.dto.LoginResponse;
import br.edu.infnet.assessment.dto.RegisterResponse;
import br.edu.infnet.assessment.dto.TokenResponse;
import br.edu.infnet.assessment.dto.UserResponse;
import br.edu.infnet.assessment.entities.SystemUser;
import br.edu.infnet.assessment.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/users")
    public ResponseEntity<Collection<SystemUser>> list() {
        return ResponseEntity.ok(authenticationService.list());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterResponse registerResponse) {
        UserResponse newUser = authenticationService.register(registerResponse);

        if (newUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create user");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginResponse loginResposta) {
        TokenResponse token = authenticationService.login(loginResposta);
        return ResponseEntity.ok(token);
    }

}
