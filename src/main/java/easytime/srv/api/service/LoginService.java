package easytime.srv.api.service;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.infra.security.TokenDto;
import easytime.srv.api.infra.security.TokenService;
import easytime.srv.api.model.user.DTOUsuario;
import easytime.srv.api.tables.User;
import easytime.srv.api.tables.repositorys.UserRepository;
import easytime.srv.api.validacoes.login.ValidacaoLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class LoginService {

    @Autowired
    private List<ValidacaoLogin> validacoes;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    public TokenDto login(DTOUsuario usuario) {

        validacoes.forEach(v -> v.validar(usuario));

        var authenticationToken = new UsernamePasswordAuthenticationToken(usuario.login(), usuario.senha());
        var authentication = manager.authenticate(authenticationToken);
        User user = userRepository.findByLogin(usuario.login()).orElseThrow();
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        String token = tokenService.gerarToken(usuario);
        return new TokenDto(token, user.getRole());
    }
}
