package easytime.srv.api.validacoes.Login;

import easytime.srv.api.infra.exceptions.CampoInvalidoException;
import easytime.srv.api.infra.exceptions.CampoVazioException;
import easytime.srv.api.model.DTOUsuario;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoUsuario implements ValidacaoLogin {
    public void validar(DTOUsuario dto) {
        if(dto.usuario().isEmpty() || dto.usuario().isBlank()) {
            throw new CampoVazioException("O usuario não pode ser vazio");
        }
        if(!dto.usuario().matches(".*[a-zA-Z].*")) {
            throw new CampoInvalidoException("Formato do usuario inválido");
        }
    }
}
