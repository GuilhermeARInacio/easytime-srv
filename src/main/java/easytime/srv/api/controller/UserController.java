package easytime.srv.api.controller;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.infra.exceptions.ObjectAlreadyExistsException;
import easytime.srv.api.model.user.UserDTO;
import easytime.srv.api.service.UserService;
import easytime.srv.api.tables.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(summary = "Criar usuário", description = "Envia um UserDTO para api e cria um usuário no banco de dados")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<Object> createUser(@RequestBody UserDTO user) {
        try{
            User userCreated = userService.createUser(user);
            return ResponseEntity.status(201).body(userCreated);
        }catch (InvalidUserException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (ObjectAlreadyExistsException e){
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Listar todos usuários", description = "Sistema retorna todos os usuários cadastrados")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<Object> listUsers() {
        List<User> users = userService.listUsers();
        if(users.isEmpty()){
            return ResponseEntity.status(404).body("Nenhum usuário cadastrado.");
        }
        return  ResponseEntity.status(200).body(users);
    }

    @GetMapping("/getById/{id}")
    @Operation(summary = "Listar usuário por id", description = "Sistema retorna um usuário pelo id")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<Object> getUserById(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        return user.<ResponseEntity<Object>>map(value -> ResponseEntity.status(200).body(value)).orElseGet(() -> ResponseEntity.status(404).body("Usuário não encontrado"));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Deletar usuário", description = "Usuário deleta um usuário pelo id")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        if(user.isEmpty()){
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok().body("Usuário apagado");
    }
}