package easytime.srv.api.validacoes.user;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.model.user.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserIsValid implements ValidacaoUser{
    @Override
    public void validar(UserDTO dto) {
        if(!dto.isValid()){
            throw new InvalidUserException("Usuário inválido");
        }
    }
}
