package easytime.srv.api.validacoes.user;

import easytime.srv.api.model.user.DTOUsuario;
import easytime.srv.api.model.user.UserDTO;

public interface ValidacaoUser {
    void validar(UserDTO dto);
}
