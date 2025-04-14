package easytime.srv_api.validacoes.Login;

import easytime.srv_api.infra.exceptions.UsuarioESenhaNotFoundException;
import easytime.srv_api.model.DTOUsuario;
import easytime.srv_api.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoUsuarioESenhaExists implements ValidacaoLogin{

    @Override
    public void validar(DTOUsuario usuario) {
        var usuarioBusca = Usuario.buscarPorLoginESenha(usuario.login(), usuario.senha());
        if(usuarioBusca == null){
            throw new UsuarioESenhaNotFoundException("O usuário e senha informados são inválidos!");
        }
    }
}
