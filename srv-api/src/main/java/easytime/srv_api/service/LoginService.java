package easytime.srv_api.service;

import easytime.srv_api.infra.security.TokenService;
import easytime.srv_api.model.DTOUsuario;
import easytime.srv_api.model.Usuario;
import easytime.srv_api.validacoes.Login.ValidacaoLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class LoginService {

    @Autowired
    private List<ValidacaoLogin> validacoes;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    public String login(@RequestBody DTOUsuario usuario) {

        validacoes.forEach(v -> v.validar(usuario));

        var authenticationToken = new UsernamePasswordAuthenticationToken(usuario.login(), usuario.senha());
        var authentication = manager.authenticate(authenticationToken);

        Usuario usuarioAutenticacao = (Usuario) authentication.getPrincipal();
        return tokenService.gerarToken(new DTOUsuario(usuarioAutenticacao.getLogin(), usuarioAutenticacao.getSenha()));
    }
}
