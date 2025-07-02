package easytime.srv.api.controller;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.model.Status;
import easytime.srv.api.model.pontos.*;
import easytime.srv.api.model.user.DTOUsuario;
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
import org.springframework.http.HttpStatus;
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
                                        "userLogin":"user"
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
                                                "userLogin":"user"
                                            }
                                            """
                            )
                    )
            )
    })
    @Operation(summary = "Bater ponto", description = "Usuário envia apenas o login e o ponto é registrado com a data e hora atuais.")
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
                    description = "Ponto excluído com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "ID não encontrado."
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autorizado."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor."
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
            @ApiResponse(responseCode = "404", description = "Caso não existam pontos salvos no BD"),
            @ApiResponse(responseCode = "500", description = "Erro interno de servidor")
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

    @PutMapping("/pedidos/status")
    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Lista os pedidos de acordo com o status informado.", description = "Retorna uma lista com os pedidos com os status informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista os pontos pendentes com sucesso"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> listarPedidosPorStatus(@RequestBody ConsultaStatus dto) {
        try {
            List<PedidoPontoDto> pedidos = pontoService.listarPedidosPorStatus(dto.status());
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao listar pedidos pendentes: " + e.getMessage());
        }
    }

    @PutMapping("/pedidos/periodo")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista os pedidos encontrados."
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Erro de autenticação."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhum pedido encontrado."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request inválida"
            )
    })
    @Operation(summary = "Lista os pedidos feitos entre as datas informadas.", description = "Usuário envia o período desejado para consultar os pedidos de ponto.")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<?> listarPedidosPorPeriodo(@Valid @RequestBody ConsultaPontosDto dto, @RequestHeader("Authorization") String token) {
        try{
            List<PedidoPontoDto> response = pontoService.listarPedidosPorPeriodo(dto, token);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | DateTimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InvalidUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autorizado.");
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Erro ao consultar pontos: " + e.getMessage());
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
            @ApiResponse(responseCode = "500", description = "Erro interno de servidor")
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
    @Operation(summary = "Reprova pedido de ponto", description = "Usuário admin reprova o pedido de ponto pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido reprovado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Request inválida"),
            @ApiResponse(responseCode = "401", description = "Usuário não autorizado ou sem permissão"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno de servidor")
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
            @ApiResponse(responseCode = "500", description = "Erro interno de servidor")
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
    @Operation(summary = "Consultar pedido de ponto", description = "Usuário consulta um pedido de alteração pelo ID do ponto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido consultado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Request inválida"),
            @ApiResponse(responseCode = "401", description = "Usuário não autorizado ou sem permissão"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno de servidor")
    })
    public ResponseEntity<?> consultarPedidoIdPonto(@PathVariable Integer idPonto, @RequestHeader("Authorization") String token) {
        try {
            AlterarPontoDto response = pontoService.consultarPedidoId(idPonto);
            return ResponseEntity.ok(response);
        }catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Erro ao consulta pedido de alteração: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao consulta pedido de alteração: " + e.getMessage());
        }
    }
}
