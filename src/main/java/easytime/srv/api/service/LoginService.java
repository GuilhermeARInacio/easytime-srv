package easytime.srv.api.service;

import easytime.srv.api.infra.security.TokenDto;
import easytime.srv.api.infra.security.TokenService;
import easytime.srv.api.model.user.DTOUsuario;
import easytime.srv.api.validacoes.Login.ValidacaoLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {

    @Autowired
    private List<ValidacaoLogin> validacoes;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    public TokenDto login( DTOUsuario usuario) {

        validacoes.forEach(v -> v.validar(usuario));

        var authenticationToken = new UsernamePasswordAuthenticationToken(usuario.login(), usuario.senha());
        var authentication = manager.authenticate(authenticationToken);

        String usuarioAutenticacao = (String) authentication.getPrincipal();
        String token = tokenService.gerarToken(new DTOUsuario(usuarioAutenticacao, usuarioAutenticacao));
        return new TokenDto(token);
    }
}
