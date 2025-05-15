package easytime.srv.api.service;


import easytime.srv.api.model.user.UserDTO;
import easytime.srv.api.model.user.UsuarioRetornoDto;
import easytime.srv.api.tables.repositorys.UserRepository;
import easytime.srv.api.tables.User;
import easytime.srv.api.validacoes.user.ValidacaoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private List<ValidacaoUser> validacoes;

    public User createUser(UserDTO user) {
        validacoes.forEach(validacao -> validacao.validar(user));
        return userRepository.save(User.toEntity(user));
    }

    public List<UsuarioRetornoDto> listUsers() {
        return userRepository.findAll()
                .stream()
                .map(UsuarioRetornoDto::new)
                .toList();
    }

    public Optional<UsuarioRetornoDto> getUserById(Integer id) {
        return userRepository.findById(id).map(UsuarioRetornoDto::new);
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}