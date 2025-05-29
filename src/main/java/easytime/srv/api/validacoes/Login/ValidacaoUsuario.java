package easytime.srv.api.validacoes.Login;

import easytime.srv.api.infra.exceptions.CampoVazioException;
import easytime.srv.api.model.user.DTOUsuario;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoUsuario implements ValidacaoLogin {
    public void validar(DTOUsuario dto) {
        if(dto.login().isEmpty() || dto.login().isBlank()) {
            throw new CampoVazioException("O usuario não pode ser vazio");
        }
        if(!dto.login().matches("^[a-zA-Z0-9._]+$")){
            throw new CampoVazioException("O usuário só pode conter letras, números, pontos e underlines");
        }
    }
}
