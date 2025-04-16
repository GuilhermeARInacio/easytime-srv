package easytime.srv.api.controller;

import easytime.srv.api.infra.exceptions.UsuarioESenhaNotFoundException;
import easytime.srv.api.model.DTOUsuario;
import easytime.srv.api.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private  LoginService loginService;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody DTOUsuario usuario) {
        try{
            var token = loginService.login(usuario);

            return ResponseEntity.status(200).body(token);
        }catch(UsuarioESenhaNotFoundException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/")
    public String hello(){
        return "Hello World";
    }
}
