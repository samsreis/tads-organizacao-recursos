package br.edu.senac.recursos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtService jwtService;

    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // Sonar S4502: CSRF desabilitado intencionalmente.
    // API stateless (SessionCreationPolicy.STATELESS) com autenticacao por JWT
    // no cabecalho Authorization. Nao ha cookie de sessao, portanto nao
    // existe vetor de ataque CSRF. Decisao alinhada a orientacao do Spring Security.
    @SuppressWarnings("java:S4502")
    @Bean
    SecurityFilterChain filtro(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(e -> e
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .authorizeHttpRequests(a -> a
                .requestMatchers("/", "/index.html", "/style.css", "/app.js", "/auth/**", "/actuator/health", "/error").permitAll()

                .requestMatchers(HttpMethod.GET, "/salas/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/salas/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/salas/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/salas/**").hasRole("ADMINISTRADOR")

                .requestMatchers(HttpMethod.GET, "/professores/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/professores/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/professores/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/professores/**").hasRole("ADMINISTRADOR")

                .requestMatchers(HttpMethod.POST, "/materiais/*/retiradas").hasAnyRole("RESPONSAVEL", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/materiais/*/devolucoes").hasAnyRole("RESPONSAVEL", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/materiais/*/saldo").authenticated()
                .requestMatchers(HttpMethod.GET, "/materiais/*/movimentacoes").hasAnyRole("RESPONSAVEL", "ADMINISTRADOR")

                .requestMatchers(HttpMethod.GET, "/materiais/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/materiais/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/materiais/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/materiais/**").hasRole("ADMINISTRADOR")

                .requestMatchers(HttpMethod.GET, "/reservas").hasAnyRole("RESPONSAVEL", "ADMINISTRADOR")
                .requestMatchers("/reservas/*/aprovar").hasAnyRole("RESPONSAVEL", "ADMINISTRADOR")
                .requestMatchers("/reservas/*/rejeitar").hasAnyRole("RESPONSAVEL", "ADMINISTRADOR")
                .requestMatchers("/reservas/*/iniciar").hasAnyRole("RESPONSAVEL", "ADMINISTRADOR")
                .requestMatchers("/reservas/*/concluir").hasAnyRole("RESPONSAVEL", "ADMINISTRADOR")
                .requestMatchers("/reservas/*/nao-compareceu").hasAnyRole("RESPONSAVEL", "ADMINISTRADOR")
                .requestMatchers("/reservas/*/historico").hasAnyRole("RESPONSAVEL", "ADMINISTRADOR")

                .requestMatchers("/relatorios/**").hasAnyRole("RESPONSAVEL", "ADMINISTRADOR")
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtFiltro(jwtService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception {
        return c.getAuthenticationManager();
    }
}
