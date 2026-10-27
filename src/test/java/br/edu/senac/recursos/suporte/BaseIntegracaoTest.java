package br.edu.senac.recursos.suporte;

import br.edu.senac.recursos.domain.Perfil;
import br.edu.senac.recursos.domain.Sala;
import br.edu.senac.recursos.domain.Usuario;
import br.edu.senac.recursos.repository.ReservaRepository;
import br.edu.senac.recursos.repository.SalaRepository;
import br.edu.senac.recursos.repository.UsuarioRepository;
import br.edu.senac.recursos.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseIntegracaoTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("recursos")
            .withUsername("recursos")
            .withPassword("recursos");

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired protected MockMvc mvc;
    @Autowired protected JwtService jwt;
    @Autowired protected UsuarioRepository usuarios;
    @Autowired protected SalaRepository salas;
    @Autowired protected ReservaRepository reservas;
    @Autowired protected PasswordEncoder encoder;

    protected Long salaId;
    protected String tokenSol;
    protected String tokenRes;
    protected String tokenAdm;

    @BeforeEach
    void preparar() {
        if (usuarios.findByEmail("sol@teste.com").isEmpty()) {
            criar("sol@teste.com", "Solicitante", Perfil.SOLICITANTE);
            criar("res@teste.com", "Responsavel", Perfil.RESPONSAVEL);
            criar("adm@teste.com", "Administrador", Perfil.ADMINISTRADOR);
            criar("sol2@teste.com", "Outro", Perfil.SOLICITANTE);
        }
        Sala base = salas.findAll().stream()
                .filter(s -> "Sala Base".equals(s.getNome()))
                .findFirst()
                .orElseGet(() -> {
                    Sala s = new Sala();
                    s.setNome("Sala Base");
                    s.setCapacidade(30);
                    s.setLocalizacao("Bloco A");
                    s.setEmManutencao(false);
                    return salas.save(s);
                });
        salaId = base.getId();
        reservas.deleteAll();
        tokenSol = jwt.gerar("sol@teste.com", "SOLICITANTE");
        tokenRes = jwt.gerar("res@teste.com", "RESPONSAVEL");
        tokenAdm = jwt.gerar("adm@teste.com", "ADMINISTRADOR");
    }

    private void criar(String email, String nome, Perfil perfil) {
        Usuario u = new Usuario();
        u.setEmail(email);
        u.setSenhaHash(encoder.encode("123456"));
        u.setNome(nome);
        u.setPerfil(perfil);
        usuarios.save(u);
    }
}
