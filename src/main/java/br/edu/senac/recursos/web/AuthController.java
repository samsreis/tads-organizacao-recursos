package br.edu.senac.recursos.web;

import br.edu.senac.recursos.domain.Usuario;
import br.edu.senac.recursos.repository.UsuarioRepository;
import br.edu.senac.recursos.security.JwtService;
import br.edu.senac.recursos.web.dto.LoginRequest;
import br.edu.senac.recursos.web.dto.LoginResponse;
import br.edu.senac.recursos.web.dto.RegistroRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(UsuarioRepository usuarios, PasswordEncoder encoder, JwtService jwt) {
        this.usuarios = usuarios;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroRequest req) {
        if (usuarios.findByEmail(req.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "E-mail já cadastrado"));
        }
        Usuario u = new Usuario();
        u.setEmail(req.email());
        u.setSenhaHash(encoder.encode(req.senha()));
        u.setNome(req.nome());
        u.setPerfil(req.perfil());
        usuarios.save(u);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", u.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        Optional<Usuario> encontrado = usuarios.findByEmail(req.email());
        if (encontrado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("erro", "Credenciais inválidas"));
        }
        Usuario u = encontrado.get();
        if (!encoder.matches(req.senha(), u.getSenhaHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("erro", "Credenciais inválidas"));
        }
        String token = jwt.gerar(u.getEmail(), u.getPerfil().name());
        return ResponseEntity.ok(new LoginResponse(token, u.getPerfil().name()));
    }
}
