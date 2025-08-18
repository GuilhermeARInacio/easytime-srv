package easytime.srv.api.validacoes.user;

import easytime.srv.api.infra.exceptions.ObjectAlreadyExistsException;
import easytime.srv.api.model.user.UserDTO;
import easytime.srv.api.tables.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoUsuarioExiste implements ValidacaoUser{

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validar(UserDTO usuario) {
        var user1 = userRepository.findByEmail(usuario.getEmail());
        var user2 = userRepository.findByLogin(usuario.getLogin());
        if(user1.isPresent()){
            throw new ObjectAlreadyExistsException("Desculpe, já existe um usuário cadastrado com este e-mail.");
        }
        if(user2.isPresent()){
            throw new ObjectAlreadyExistsException("Desculpe, já existe um usuário cadastrado com este login.");
        }
    }
}
