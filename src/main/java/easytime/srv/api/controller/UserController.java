package easytime.srv.api.controller;

import easytime.srv.api.model.UserDTO;
import easytime.srv.api.service.UserService;
import easytime.srv.api.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody UserDTO user) {
        if (!user.isValid()) {
            return ResponseEntity.badRequest().body("Usuário inválido");
        }
        User userCreated = userService.createUser(user);
        return ResponseEntity.status(201).body(userCreated);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listUsers() {
        List<User> users = userService.listUsers();
        if(users.isEmpty()){
            return ResponseEntity.status(404).body("Nenhum usuário cadastrado.");
        }
        return  ResponseEntity.status(200).body(users);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        return user.<ResponseEntity<Object>>map(value -> ResponseEntity.status(200).body(value)).orElseGet(() -> ResponseEntity.status(404).body("Usuário não encontrado"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        if(user.isEmpty()){
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok().body("Usuário apagado");
    }
}