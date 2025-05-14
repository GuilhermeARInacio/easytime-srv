package easytime.srv.api.controller;

import easytime.srv.api.model.pontos.TimeLogDto;
import easytime.srv.api.model.user.DTOUsuario;
import easytime.srv.api.model.user.LoginDto;
import easytime.srv.api.service.PontoService;
import easytime.srv.api.tables.TimeLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

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
                    responseCode = "404",
                    description = "Usuário inválido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Retorna o código 404 e a mensagem de erro",
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
            LocalDate dataHoje = LocalDate.now();
            Time horaAgora = Time.valueOf(LocalTime.now());

            var ponto = pontoService.registrarPonto(login, dataHoje, horaAgora);
            return ResponseEntity.ok(ponto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar ponto: " + e.getMessage());
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
        pontoService.removerPonto(id);
        return ResponseEntity.ok().build();
    }
}
