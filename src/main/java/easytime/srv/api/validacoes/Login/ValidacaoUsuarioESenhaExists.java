package easytime.srv.api.validacoes.Login;

import easytime.srv.api.infra.exceptions.UsuarioESenhaNotFoundException;
import easytime.srv.api.model.DTOUsuario;
import easytime.srv.api.model.Usuario;
import easytime.srv.api.tables.repositorys.UserRepository;
import easytime.srv.api.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoUsuarioESenhaExists implements ValidacaoLogin{

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validar(DTOUsuario usuario) {
        var user = userRepository.findByLogin(usuario.login());

        if(user.isEmpty()){
            throw new UsuarioESenhaNotFoundException("O usuário informado é inválido!");
        }
        if(!PasswordUtil.validatePassword(usuario.senha(), user.get().getPassword())){
            throw new UsuarioESenhaNotFoundException("Senha inválida");
        }
    }
}
