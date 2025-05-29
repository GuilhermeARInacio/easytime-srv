package easytime.srv.api.controller;

import easytime.srv.api.infra.exceptions.CampoInvalidoException;
import easytime.srv.api.infra.exceptions.CampoVazioException;
import easytime.srv.api.infra.exceptions.UsuarioESenhaNotFoundException;
import easytime.srv.api.infra.security.TokenDto;
import easytime.srv.api.model.user.DTOUsuario;
import easytime.srv.api.service.LoginService;
import easytime.srv.api.tables.repositorys.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  LoginService loginService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário validado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DTOUsuario.class),
                            examples = @ExampleObject(
                                    name = "Retorna um token JWT sempre que o usuário é validado com sucesso",
                                    value = """
                            {
                                "usuario":"mkenzo",
                                "senha":"Mat@@1234"
                            }
                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário ou senha inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Retorna o código 401 e a mensagem de erro",
                                    value = """
                            {
                                "usuario": "abc@gmail.com",
                                "senha": "12344"
                            }
                            """
                            )
                    )
            )
    })
    @Operation(summary = "Fazer login", description = "Usuário envia usuário e senha válidos para obter um token JWT")
    public ResponseEntity<?> login(@RequestBody DTOUsuario usuario) {
        try{
            var token = loginService.login(usuario);

            return ResponseEntity.status(200).body(token);
        }catch (CampoInvalidoException | CampoVazioException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }catch(UsuarioESenhaNotFoundException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
