package easytime.srv.api.validacoes.login;

import easytime.srv.api.infra.exceptions.CampoInvalidoException;
import easytime.srv.api.infra.exceptions.CampoVazioException;
import easytime.srv.api.model.user.DTOUsuario;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoSenha implements ValidacaoLogin {
    public void validar(DTOUsuario dto) {
        Boolean senhaVazia = dto.senha().isEmpty() || dto.senha().isBlank();
        Boolean tamanhoInvalido = dto.senha().length() < 8;
        Boolean temLetras = dto.senha().matches(".*[a-zA-Z].*");
        Boolean temNumeros = dto.senha().matches(".*\\d.*");
        Boolean temCaracteresEspeciais = dto.senha().matches(".*[!@#$%^&*(),.?\":{}|<>].*");

        if(senhaVazia) {
            throw new CampoVazioException("A senha não pode ser vazia");
        }
        if(tamanhoInvalido || !temLetras || !temNumeros || !temCaracteresEspeciais) {
            throw new CampoInvalidoException("Formato de senha inválido");
        }
    }
}

