package easytime.srv_api.controller;

import easytime.srv_api.infra.exceptions.UsuarioESenhaNotFoundException;
import easytime.srv_api.model.DTOUsuario;
import easytime.srv_api.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody DTOUsuario usuario) {
        try{
            var token = loginService.login(usuario);

            return ResponseEntity.ok().body(token);
        }catch(UsuarioESenhaNotFoundException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/")
    public String hello(){
        return "Hello World";
    }
}
