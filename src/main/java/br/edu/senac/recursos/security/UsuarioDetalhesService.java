package br.edu.senac.recursos.security;

import br.edu.senac.recursos.domain.Usuario;
import br.edu.senac.recursos.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioDetalhesService implements UserDetailsService {

    private final UsuarioRepository usuarios;

    public UsuarioDetalhesService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario u = usuarios.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
        return new User(u.getEmail(), u.getSenhaHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + u.getPerfil().name())));
    }
}
