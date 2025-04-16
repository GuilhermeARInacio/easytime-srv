package easytime.srv.api.validacoes.Login;

import easytime.srv.api.infra.exceptions.UsuarioESenhaNotFoundException;
import easytime.srv.api.model.DTOUsuario;
import easytime.srv.api.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoUsuarioESenhaExists implements ValidacaoLogin{

    @Override
    public void validar(DTOUsuario usuario) {
        var usuarioBusca = Usuario.buscarPorLoginESenha(usuario.usuario(), usuario.senha());
        if(usuarioBusca == null){
            throw new UsuarioESenhaNotFoundException("O usuário e senha informados são inválidos!");
        }
    }
}
