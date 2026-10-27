package br.edu.senac.recursos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey chave;
    private final long validadeMs;

    public JwtService(@Value("${app.jwt.secret}") String segredo,
                      @Value("${app.jwt.validade-ms:3600000}") long validadeMs) {
        this.chave = Keys.hmacShaKeyFor(segredo.getBytes());
        this.validadeMs = validadeMs;
    }

    public String gerar(String email, String perfil) {
        Date agora = new Date();
        return Jwts.builder()
                .subject(email)
                .claim("perfil", perfil)
                .issuedAt(agora)
                .expiration(new Date(agora.getTime() + validadeMs))
                .signWith(chave)
                .compact();
    }

    public Claims extrair(String token) {
        return Jwts.parser().verifyWith(chave).build()
                .parseSignedClaims(token).getPayload();
    }
}
