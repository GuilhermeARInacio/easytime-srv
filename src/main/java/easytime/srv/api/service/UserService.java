package easytime.srv.api.service;


import easytime.srv.api.model.UserDTO;
import easytime.srv.api.tables.repositorys.UserRepository;
import easytime.srv.api.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(UserDTO user) {
        return userRepository.save(User.toEntity(user));
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}