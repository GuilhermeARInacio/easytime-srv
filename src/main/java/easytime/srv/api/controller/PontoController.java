package easytime.srv.api.controller;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.model.pontos.ConsultaPontosDto;
import easytime.srv.api.model.pontos.RegistroCompletoDto;
import easytime.srv.api.model.user.DTOUsuario;
import easytime.srv.api.model.user.LoginDto;
import easytime.srv.api.service.PontoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;

@Controller
@RestController
@RequestMapping("/ponto")
public class PontoController {

    @Autowired
    private PontoService pontoService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ponto batido com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DTOUsuario.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "login":"mkenzo"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autorizado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Retorna o código 401 e a mensagem de erro",
                                    value = """
                                            {
                                                "login":"abdc"
                                            }
                                            """
                            )
                    )
            )
    })
    @Operation(summary = "Bater ponto", description = "Usuário envia apenas o login e o ponto é registrado com a data e hora atuais.")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<?> registrarPonto(@RequestBody LoginDto login) {
        try{
            var ponto = pontoService.registrarPonto(login);
            return ResponseEntity.ok(ponto);
        } catch (NotFoundException e) {
            return ResponseEntity.status(401).body("Erro ao registrar ponto: " + e.getMessage());
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro ao registrar ponto: " + e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao registrar ponto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ponto excluido com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "ID não encontrado"
            )
    })
    @Operation(summary = "Remover registro de batimento de ponto", description = "Usuário envia o ID do registro.")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<?> removerPonto(@PathVariable Integer id) {
        try{
            pontoService.removerPonto(id);
            return ResponseEntity.ok().body("Ponto removido com sucesso.");
        }catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Erro ao remover ponto: " + e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao remover ponto: " + e.getMessage());
        }
    }

    @PutMapping("/consulta")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pontos encontrados"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pontos não encontrados"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não encontrado ou não autorizado"
            )
    })
    @Operation(summary = "Listar registros de batimento de ponto de um certo periodo.", description = "Usuário envia login, data de inicio e data final do periodo.")
    @SecurityRequirement(name = "bearer-key")
    @PostMapping("/consulta")
    public ResponseEntity<?> consultar(@Valid @RequestBody ConsultaPontosDto dto){
        try{
            List<RegistroCompletoDto> response = pontoService.consultar(dto);

            return ResponseEntity.ok(response);
        } catch (NotFoundException e){
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (InvalidUserException e){
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Erro ao consultar pontos: " + e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> listarPontos() {
        try{
            List<RegistroCompletoDto> pontos = pontoService.listarPontos();
            return ResponseEntity.ok(pontos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao listar pontos: " + e.getMessage());
        }
    }
}
