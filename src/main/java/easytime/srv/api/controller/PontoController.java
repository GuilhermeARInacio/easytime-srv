package easytime.srv.api.controller;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.model.pontos.*;
import easytime.srv.api.model.user.DTOUsuario;
import easytime.srv.api.model.user.LoginDto;
import easytime.srv.api.service.PontoService;
import easytime.srv.api.tables.PedidoPonto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.time.DateTimeException;
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
                                        "userLogin":"mkenzo"
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
                                                "userLogin":"abdc"
                                            }
                                            """
                            )
                    )
            )
    })
    @Operation(summary = "Bater ponto", description = "Usuário envia apenas o userLogin e o ponto é registrado com a data e hora atuais.")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<?> registrarPonto(@RequestBody BaterPonto dto, @RequestHeader("Authorization") String token) {
        try{
            var ponto = pontoService.registrarPonto(dto, token);
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
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autorizado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor"
            )
    })
    @Operation(summary = "Remover registro de batimento de ponto", description = "Usuário envia o ID do registro.")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<?> removerPonto(@PathVariable Integer id, @RequestHeader("Authorization") String token) {
        try{
            pontoService.removerPonto(id, token);
            return ResponseEntity.ok().body("Ponto removido com sucesso.");
        }catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao remover ponto: " + e.getMessage());
        }catch (IllegalCallerException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erro ao remover ponto: " + e.getMessage());
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
                    responseCode = "401",
                    description = "Login inválido"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Datas não encontradas"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request inválida"
            )
    })
    @Operation(summary = "Remover registro de batimento de ponto", description = "Usuário envia o ID do registro.")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<?> consultar(@Valid @RequestBody ConsultaPontosDto dto, @RequestHeader("Authorization") String token) {
        try{
            List<RegistroCompletoDto> response = pontoService.consultar(dto, token);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | DateTimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InvalidUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário inválido");
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Erro ao consultar pontos: " + e.getMessage());
        }
    }

    @PutMapping("/alterar")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registro alterado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autorizado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Campos não encontrados"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request inválida"
            )
    })
    @Operation(summary = "Alterar registro de ponto", description = "Altera um registro de ponto do usuário.")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<?> alterarPonto(@Valid @RequestBody AlterarPontoDto dto, @RequestHeader("Authorization") String token){
        try{
            pontoService.alterarPonto(dto, token);
            return ResponseEntity.ok("Pedido de alteração criado com sucesso. Aguarde aprovação do gestor.");
        } catch (NotFoundException e){
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalArgumentException | DateTimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InvalidUserException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Erro ao alterar ponto: " + e.getMessage());
        }
    }

    @GetMapping
    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Lista todos os pontos salvos", description = "Retorna uma lista com todos os pontos salvos no BD.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista com os pontos retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não existiam pontos salvos no BD"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> listarPontos() {
        try {
            List<RegistroCompletoDto> pontos = pontoService.listarPontos();
            return ResponseEntity.ok(pontos);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao listar pontos: " + e.getMessage());
        }
    }

    @GetMapping("/pedidos/pendentes")
    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Lista os pedidos pendentes.", description = "Retorna uma lista com os pedidos pendentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista os pontos pendentes com sucesso"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> listarPedidosPendentes() {
        try {
            List<PedidoPontoDto> pedidos = pontoService.listarPedidoPendentes();
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao listar pedidos pendentes: " + e.getMessage());
        }
    }

    @PostMapping("/aprovar/{idPedido}")
    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Aprova um pedido de ponto", description = "Aprova um pedido de ponto pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido aprovado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Request inválida"),
            @ApiResponse(responseCode = "401", description = "Usuário não autorizado ou sem permissão"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> aprovarPonto(@PathVariable Integer idPedido, @RequestHeader("Authorization") String token) {
        try {
            var mensagem = pontoService.aprovarPonto(idPedido, token);
            return ResponseEntity.ok(mensagem);
        }catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Erro ao aprovar ponto: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }catch (InvalidUserException | IllegalCallerException e ) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao aprovar ponto: " + e.getMessage());
        }
    }

    @PostMapping("/reprovar/{idPedido}")
    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Reprova pedido de ponto", description = "Usuário admin reprova o pedido de ponto pelo ID  .")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido reprovado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Request inválida"),
            @ApiResponse(responseCode = "401", description = "Usuário não autorizado ou sem permissão"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> reprovarPonto(@PathVariable Integer idPedido, @RequestHeader("Authorization") String token) {
        try {
            var mensagem = pontoService.reprovarPonto(idPedido, token);
            return ResponseEntity.ok(mensagem);
        }catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Erro ao reprovar ponto: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }catch (InvalidUserException | IllegalCallerException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao reprovar ponto: " + e.getMessage());
        }
    }

    @GetMapping("/pedidos/all")
    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Lista todos os pedidos", description = "Retorna uma lista com todos os pedidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista com todos os pedidos retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> listAllPedidos(){
        try {
            List<PedidoPontoDto> pedidos = pontoService.listarAllPedidos();
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao listar todos os pedidos: " + e.getMessage());
        }
    }

    @GetMapping("/pedido/{idPonto}")
    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "r pedido de ponto", description = "Usuário consulta um pedido de alteração.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido consultado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Request inválida"),
            @ApiResponse(responseCode = "401", description = "Usuário não autorizado ou sem permissão"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> consultarPedidoId(@PathVariable Integer idPonto, @RequestHeader("Authorization") String token) {
        try {
            AlterarPontoDto response = pontoService.consultarPedidoId(idPonto);
            return ResponseEntity.ok(response);
        }catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Erro ao consulta pedido de alteração: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }catch (InvalidUserException | IllegalCallerException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao consulta pedido de alteração: " + e.getMessage());
        }
    }
}
