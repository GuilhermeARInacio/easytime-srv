package easytime.srv.api.service;

import easytime.srv.api.infra.exceptions.UsuarioESenhaNotFoundException;
import easytime.srv.api.infra.security.TokenService;
import easytime.srv.api.model.DTOUsuario;
import easytime.srv.api.model.Usuario;
import easytime.srv.api.tables.User;
import easytime.srv.api.util.PasswordUtil;
import easytime.srv.api.validacoes.Login.ValidacaoLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private List<ValidacaoLogin> validacoes;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    UserService userService;

    @Autowired
    private TokenService tokenService;

    public String login( DTOUsuario usuario) {
        Optional<User> user = userService.getUserByLogin(usuario.login());

        if(user.isEmpty()){
            throw new UsuarioESenhaNotFoundException("Usuário não encontrado");
        }
        if(!PasswordUtil.validatePassword(usuario.senha(), user.get().getPassword())){
            throw new UsuarioESenhaNotFoundException("Senha inválida");
        }

        var authenticationToken = new UsernamePasswordAuthenticationToken(usuario.login(), usuario.senha());
        var authentication = manager.authenticate(authenticationToken);

        Usuario usuarioAutenticacao = (Usuario) authentication.getPrincipal();
        return tokenService.gerarToken(new DTOUsuario(usuarioAutenticacao.getLogin(), usuarioAutenticacao.getSenha()));
    }
}
