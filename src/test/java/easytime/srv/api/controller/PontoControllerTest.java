// File: src/test/java/easytime/srv/api/controller/PontoControllerTest.java
package easytime.srv.api.controller;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.model.Status;
import easytime.srv.api.model.pontos.*;
import easytime.srv.api.service.PontoService;
import easytime.srv.api.model.pontos.PedidoPontoDto;
import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.tables.TimeLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.webjars.NotFoundException;

import java.sql.Time;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PontoControllerTest {

    @Mock
    private PontoService pontoService;

    @InjectMocks
    private PontoController pontoController;

    private final String token = "token";
    private final Integer id = 1;
    private final TimeLogDto timeLogDto = new TimeLogDto(
            "mkenzo",
            "01/10/2023",
            Time.valueOf("08:00:00"),
            Status.PENDENTE
    );
    private final ConsultaPontosDto consultaPontosDto = new ConsultaPontosDto("", "");

    @Test
    void registrarPonto_Success() {
        BaterPonto dto = new BaterPonto("08:00:00");

        when(pontoService.registrarPonto(dto, token)).thenReturn(timeLogDto);

        ResponseEntity<?> response = pontoController.registrarPonto(dto, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(pontoService, times(1)).registrarPonto(dto, token);
    }

    @Test
    void registrarPonto_NotFoundException() {
        BaterPonto dto = new BaterPonto("08:00:00");
        when(pontoService.registrarPonto(dto, token)).thenThrow(new NotFoundException("not found"));

        ResponseEntity<?> response = pontoController.registrarPonto(dto, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(pontoService, times(1)).registrarPonto(dto, token);
    }

    @Test
    void registrarPonto_IllegalArgumentException() {
        BaterPonto dto = new BaterPonto("08:00:00");
        when(pontoService.registrarPonto(dto, token)).thenThrow(new IllegalArgumentException("bad arg"));

        ResponseEntity<?> response = pontoController.registrarPonto(dto, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(pontoService, times(1)).registrarPonto(dto, token);
    }

    @Test
    void registrarPonto_GenericException() {
        BaterPonto dto = new BaterPonto("08:00:00");
        when(pontoService.registrarPonto(dto, token)).thenThrow(new RuntimeException("fail"));

        ResponseEntity<?> response = pontoController.registrarPonto(dto, token);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(pontoService, times(1)).registrarPonto(dto, token);
    }

    @Test
    void removerPonto_Success() {
        doNothing().when(pontoService).removerPonto(id, token);

        ResponseEntity<?> response = pontoController.removerPonto(id, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(pontoService, times(1)).removerPonto(id, token);
    }

    @Test
    void removerPonto_NotFoundException() {
        doThrow(new NotFoundException("not found")).when(pontoService).removerPonto(id, token);

        ResponseEntity<?> response = pontoController.removerPonto(id, token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(pontoService, times(1)).removerPonto(id, token);
    }

    @Test
    void removerPonto_IllegalCallerException() {
        doThrow(new IllegalCallerException("forbidden")).when(pontoService).removerPonto(id, token);

        ResponseEntity<?> response = pontoController.removerPonto(id, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(pontoService, times(1)).removerPonto(id, token);
    }

    @Test
    void removerPonto_GenericException() {
        doThrow(new RuntimeException("fail")).when(pontoService).removerPonto(id, token);

        ResponseEntity<?> response = pontoController.removerPonto(id, token);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(pontoService, times(1)).removerPonto(id, token);
    }

    @Test
    void consultar_Success() {
        ConsultaPontosDto dto = new ConsultaPontosDto("", "");
        List<RegistroCompletoDto> list = new ArrayList<>();
        when(pontoService.consultar(dto, token)).thenReturn(list);

        ResponseEntity<?> response = pontoController.consultar(dto, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
        verify(pontoService, times(1)).consultar(dto, token);
    }

    @Test
    void consultar_IllegalArgumentException() {

        when(pontoService.consultar(any(ConsultaPontosDto.class), any(String.class))).thenThrow(new IllegalArgumentException("bad arg"));

        ResponseEntity<?> response = pontoController.consultar(consultaPontosDto, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void consultar_DateTimeException() {
         when(pontoService.consultar(any(ConsultaPontosDto.class), any(String.class))).thenThrow(new DateTimeException("bad date"));

        ResponseEntity<?> response = pontoController.consultar(consultaPontosDto, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

    @Test
    void consultar_InvalidUserException() {
        when(pontoService.consultar(any(ConsultaPontosDto.class), any(String.class))).thenThrow(new InvalidUserException("invalid"));

        ResponseEntity<?> response = pontoController.consultar(consultaPontosDto, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void consultar_GenericException() {
       when(pontoService.consultar(any(ConsultaPontosDto.class), any(String.class))).thenThrow(new RuntimeException("fail"));

        ResponseEntity<?> response = pontoController.consultar(consultaPontosDto, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void alterarPonto_Success() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        doNothing().when(pontoService).alterarPonto(dto, token);

        ResponseEntity<?> response = pontoController.alterarPonto(dto, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(pontoService, times(1)).alterarPonto(dto, token);
    }

    @Test
    void alterarPonto_NotFoundException() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        doThrow(new NotFoundException("not found")).when(pontoService).alterarPonto(dto, token);

        ResponseEntity<?> response = pontoController.alterarPonto(dto, token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(pontoService, times(1)).alterarPonto(dto, token);
    }

    @Test
    void alterarPonto_IllegalArgumentException() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        doThrow(new IllegalArgumentException("bad arg")).when(pontoService).alterarPonto(dto, token);

        ResponseEntity<?> response = pontoController.alterarPonto(dto, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(pontoService, times(1)).alterarPonto(dto, token);
    }

    @Test
    void alterarPonto_DateTimeException() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        doThrow(new DateTimeException("bad date")).when(pontoService).alterarPonto(dto, token);

        ResponseEntity<?> response = pontoController.alterarPonto(dto, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(pontoService, times(1)).alterarPonto(dto, token);
    }

    @Test
    void alterarPonto_InvalidUserException() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        doThrow(new InvalidUserException("invalid")).when(pontoService).alterarPonto(dto, token);

        ResponseEntity<?> response = pontoController.alterarPonto(dto, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(pontoService, times(1)).alterarPonto(dto, token);
    }

    @Test
    void alterarPonto_GenericException() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        doThrow(new RuntimeException("fail")).when(pontoService).alterarPonto(dto, token);

        ResponseEntity<?> response = pontoController.alterarPonto(dto, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(pontoService, times(1)).alterarPonto(dto, token);
    }

    @Test
    void listarPontos_Success() {
        List<RegistroCompletoDto> list = new ArrayList<>();
        when(pontoService.listarPontos()).thenReturn(list);

        ResponseEntity<?> response = pontoController.listarPontos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
        verify(pontoService, times(1)).listarPontos();
    }

    @Test
    void listarPontos_Exception() {
        when(pontoService.listarPontos()).thenThrow(new RuntimeException("fail"));

        ResponseEntity<?> response = pontoController.listarPontos();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(pontoService, times(1)).listarPontos();
    }

    @Test
    void filtrarPedidos_Success() {
        List<PedidoPontoDto> list = new ArrayList<>();
        FiltroPedidos dto = new FiltroPedidos("01/06/2025", "05/06/2025", Status.PENDENTE, PedidoPonto.Tipo.ALTERACAO);
        when(pontoService.filtrarPedidos(dto)).thenReturn(list);

        ResponseEntity<?> response = pontoController.filtrarPedidos(dto, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
        verify(pontoService, times(1)).filtrarPedidos(dto);
    }

    @Test
    void filtrarPedidos_BadRequest() {
        FiltroPedidos dto = new FiltroPedidos("01/06/2025", "05/06/2025", Status.PENDENTE, PedidoPonto.Tipo.ALTERACAO);
        when(pontoService.filtrarPedidos(dto)).thenThrow(new RuntimeException("fail"));

        ResponseEntity<?> response = pontoController.filtrarPedidos(dto, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(pontoService, times(1)).filtrarPedidos(dto);
    }

    @Test
    void filtrarPedidos_Unathorized() {
        FiltroPedidos dto = new FiltroPedidos("01/06/2025", "05/06/2025", Status.PENDENTE, PedidoPonto.Tipo.ALTERACAO);
        when(pontoService.filtrarPedidos(dto)).thenThrow(new InvalidUserException("fail"));

        ResponseEntity<?> response = pontoController.filtrarPedidos(dto, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(pontoService, times(1)).filtrarPedidos(dto);
    }

    @Test
    void filtrarPedidos_IllegalArgument() {
        FiltroPedidos dto = new FiltroPedidos("01/06/2025", "05/06/2025", Status.PENDENTE, PedidoPonto.Tipo.ALTERACAO);
        when(pontoService.filtrarPedidos(dto)).thenThrow(new IllegalArgumentException("fail"));

        ResponseEntity<?> response = pontoController.filtrarPedidos(dto, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(pontoService, times(1)).filtrarPedidos(dto);
    }

    @Test
    void aprovarPonto_Success() {
       when(pontoService.aprovarPonto(anyInt(), anyString())).thenReturn("Ponto aprovado");

        var response = pontoController.aprovarPonto(id, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(pontoService, times(1)).aprovarPonto(id, token);
    }

    @Test
    void aprovarPonto_NotFoundException() {
        doThrow(new NotFoundException("not found")).when(pontoService).aprovarPonto(id, token);

        ResponseEntity<?> response = pontoController.aprovarPonto(id, token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(pontoService, times(1)).aprovarPonto(id, token);
    }

    @Test
    void aprovarPonto_IllegalArgumentException() {
        doThrow(new IllegalArgumentException("bad arg")).when(pontoService).aprovarPonto(id, token);

        ResponseEntity<?> response = pontoController.aprovarPonto(id, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(pontoService, times(1)).aprovarPonto(id, token);
    }

    @Test
    void aprovarPonto_InvalidUserException() {
        doThrow(new InvalidUserException("invalid")).when(pontoService).aprovarPonto(id, token);

        ResponseEntity<?> response = pontoController.aprovarPonto(id, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(pontoService, times(1)).aprovarPonto(id, token);
    }

    @Test
    void aprovarPonto_IllegalCallerException() {
        doThrow(new IllegalCallerException("forbidden")).when(pontoService).aprovarPonto(id, token);

        ResponseEntity<?> response = pontoController.aprovarPonto(id, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(pontoService, times(1)).aprovarPonto(id, token);
    }

    @Test
    void aprovarPonto_GenericException() {
        doThrow(new RuntimeException("fail")).when(pontoService).aprovarPonto(id, token);

        ResponseEntity<?> response = pontoController.aprovarPonto(id, token);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(pontoService, times(1)).aprovarPonto(id, token);
    }

    @Test
    void reprovarPonto_Success() {
        when(pontoService.reprovarPonto(id, token)).thenReturn("Registro ponto reprovado pelo gestor.");

        ResponseEntity<?> response = pontoController.reprovarPonto(id, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(pontoService, times(1)).reprovarPonto(id, token);
    }

    @Test
    void reprovarPonto_NotFoundException() {
        doThrow(new NotFoundException("not found")).when(pontoService).reprovarPonto(id, token);

        ResponseEntity<?> response = pontoController.reprovarPonto(id, token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(pontoService, times(1)).reprovarPonto(id, token);
    }

    @Test
    void reprovarPonto_IllegalArgumentException() {
        doThrow(new IllegalArgumentException("bad arg")).when(pontoService).reprovarPonto(id, token);

        ResponseEntity<?> response = pontoController.reprovarPonto(id, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(pontoService, times(1)).reprovarPonto(id, token);
    }

    @Test
    void reprovarPonto_InvalidUserException() {
        doThrow(new InvalidUserException("invalid")).when(pontoService).reprovarPonto(id, token);

        ResponseEntity<?> response = pontoController.reprovarPonto(id, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(pontoService, times(1)).reprovarPonto(id, token);
    }

    @Test
    void reprovarPonto_IllegalCallerException() {
        doThrow(new IllegalCallerException("forbidden")).when(pontoService).reprovarPonto(id, token);

        ResponseEntity<?> response = pontoController.reprovarPonto(id, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(pontoService, times(1)).reprovarPonto(id, token);
    }

    @Test
    void reprovarPonto_GenericException() {
        doThrow(new RuntimeException("fail")).when(pontoService).reprovarPonto(id, token);

        ResponseEntity<?> response = pontoController.reprovarPonto(id, token);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(pontoService, times(1)).reprovarPonto(id, token);
    }

    @Test
    void listAllPedidos_Success() {
        List<PedidoPontoDto> list = new ArrayList<>();
        when(pontoService.listarAllPedidos()).thenReturn(list);

        ResponseEntity<?> response = pontoController.listAllPedidos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
        verify(pontoService, times(1)).listarAllPedidos();
    }

    @Test
    void listAllPedidos_Exception() {
        when(pontoService.listarAllPedidos()).thenThrow(new RuntimeException("fail"));

        ResponseEntity<?> response = pontoController.listAllPedidos();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(pontoService, times(1)).listarAllPedidos();
    }

    @Test
    void consultarPedidoPeloId_Success() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        when(pontoService.consultarPedidoId(dto.idPonto())).thenReturn(dto);

        ResponseEntity<?> response = pontoController.consultarPedidoIdPonto(dto.idPonto(), token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(pontoService, times(1)).consultarPedidoId(dto.idPonto());
    }

    @Test
    void consultarPedidoPeloId_Unathorized() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        when(pontoService.consultarPedidoId(dto.idPonto())).thenThrow(NotFoundException.class);

        ResponseEntity<?> response = pontoController.consultarPedidoIdPonto(dto.idPonto(), token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(pontoService, times(1)).consultarPedidoId(dto.idPonto());
    }

    @Test
    void consultarPedidoPeloId_InternalError() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        when(pontoService.consultarPedidoId(dto.idPonto())).thenThrow(RuntimeException.class);

        ResponseEntity<?> response = pontoController.consultarPedidoIdPonto(dto.idPonto(), token);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(pontoService, times(1)).consultarPedidoId(dto.idPonto());
    }
}