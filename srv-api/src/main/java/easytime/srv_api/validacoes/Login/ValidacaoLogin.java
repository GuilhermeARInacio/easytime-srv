package easytime.srv_api.validacoes.Login;

import easytime.srv_api.model.DTOUsuario;

public interface ValidacaoLogin {
    void validar(DTOUsuario usuario);
}
