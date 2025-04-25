package easytime.srv.api.infra.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String login = authentication.getName();
        String senha = authentication.getCredentials().toString();

        //Usuario usuario = Usuario.buscarPorLoginESenha(login, senha);

//        if (usuario == null) {
//            throw new BadCredentialsException("Login ou senha inválidos!");
//        }

        return new UsernamePasswordAuthenticationToken(
            login,
                null,
                Collections.emptyList()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}