package easytime.srv.api.validacoes.login;

import easytime.srv.api.model.user.DTOUsuario;

public interface ValidacaoLogin {
    void validar(DTOUsuario usuario);
}
