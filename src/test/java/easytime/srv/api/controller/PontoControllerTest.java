package easytime.srv.api.controller;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.model.pontos.ConsultaPontosDto;
import easytime.srv.api.model.pontos.RegistroCompletoDto;
import easytime.srv.api.model.pontos.TimeLogDto;
import easytime.srv.api.model.user.LoginDto;
import easytime.srv.api.service.PontoService;
import easytime.srv.api.tables.TimeLog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.webjars.NotFoundException;

import java.sql.Time;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PontoControllerTest {

    @Mock
    private PontoService pontoService;

    @InjectMocks
    private PontoController pontoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarPonto_Success() {
        // Arrange
        LoginDto login = new LoginDto("mkenzo");
        LocalDate dataHoje = LocalDate.now();
        Time horaAgora = Time.valueOf(LocalTime.now());
        TimeLogDto timeLogDto = new TimeLogDto("mkenzo", dataHoje, horaAgora, TimeLog.Status.PENDENTE);

        when(pontoService.registrarPonto(login)).thenReturn(timeLogDto);

        // Act
        ResponseEntity<?> response = pontoController.registrarPonto(login);

        // Assert
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(pontoService, times(1)).registrarPonto(login);
    }

    @Test
    void registrarPonto_NotFoundException() {
        // Arrange
        LoginDto login = new LoginDto("invalid");
        when(pontoService.registrarPonto(any())).thenThrow(new NotFoundException("Usuário não encontrado."));

        // Act
        ResponseEntity<?> response = pontoController.registrarPonto(login);

        // Assert
        assertEquals(HttpStatusCode.valueOf(401), response.getStatusCode());
        verify(pontoService, times(1)).registrarPonto(any());
    }

    @Test
    void registrarPonto_IllegalArgumentException() {
        // Arrange
        LoginDto login = new LoginDto("mkenzo");
        when(pontoService.registrarPonto(any())).thenThrow(new IllegalArgumentException("Erro de validação."));

        // Act
        ResponseEntity<?> response = pontoController.registrarPonto(login);

        // Assert
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        verify(pontoService, times(1)).registrarPonto(any());
    }

    @Test
    void registrarPonto_InternalServerError() {
        // Arrange
        LoginDto login = new LoginDto("mkenzo");
        when(pontoService.registrarPonto(any())).thenThrow(new RuntimeException("Erro inesperado."));

        // Act
        ResponseEntity<?> response = pontoController.registrarPonto(login);

        // Assert
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        verify(pontoService, times(1)).registrarPonto(any());
    }

    @Test
    void removerPonto_Success() {
        // Arrange
        Integer id = 1;
        doNothing().when(pontoService).removerPonto(id);

        // Act
        ResponseEntity<?> response = pontoController.removerPonto(id);

        // Assert
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(pontoService, times(1)).removerPonto(id);
    }

    @Test
    void removerPonto_NotFoundException() {
        // Arrange
        Integer id = 1;
        doThrow(new NotFoundException("Ponto não encontrado.")).when(pontoService).removerPonto(id);

        // Act
        ResponseEntity<?> response = pontoController.removerPonto(id);

        // Assert
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        verify(pontoService, times(1)).removerPonto(id);
    }

    @Test
    void removerPonto_InternalServerError() {
        // Arrange
        Integer id = 1;
        doThrow(new RuntimeException("Erro inesperado.")).when(pontoService).removerPonto(id);

        // Act
        ResponseEntity<?> response = pontoController.removerPonto(id);

        // Assert
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        verify(pontoService, times(1)).removerPonto(id);
    }

    @Test
    void consultar_Success() {
        // Arrange
        ConsultaPontosDto dto = new ConsultaPontosDto("mkenzo", LocalDate.of(2025,05,01), LocalDate.of(2025,05,20));
        List<RegistroCompletoDto> registros = new ArrayList<>();
        when(pontoService.consultar(dto)).thenReturn(registros);

        // Act
        ResponseEntity<?> response = pontoController.consultar(dto);

        // Assert
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(pontoService, times(1)).consultar(dto);
    }

    @Test
    void consultar_NotFoundException() {
        // Arrange
        ConsultaPontosDto dto = new ConsultaPontosDto("mkenzo", LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5,20));
        when(pontoService.consultar(dto)).thenThrow(new NotFoundException("Registro não encontrado."));

        // Act
        ResponseEntity<?> response = pontoController.consultar(dto);

        // Assert
        verify(pontoService, times(1)).consultar(dto);
    }

    @Test
    void consultar_IllegalArgumentException() {
        // Arrange
        ConsultaPontosDto dto = new ConsultaPontosDto("mkenzo", LocalDate.of(2025,05,01), LocalDate.of(2025,05,20));
        when(pontoService.consultar(dto)).thenThrow(new IllegalArgumentException("Erro de validação."));

        // Act
        ResponseEntity<?> response = pontoController.consultar(dto);

        // Assert
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        verify(pontoService, times(1)).consultar(dto);
    }

    @Test
    void consultar_DateTimeException() {
        ConsultaPontosDto dto = new ConsultaPontosDto("user", LocalDate.now(), LocalDate.now());
        when(pontoService.consultar(dto)).thenThrow(new DateTimeException("invalid date"));

        ResponseEntity<?> response = pontoController.consultar(dto);

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        verify(pontoService, times(1)).consultar(dto);
    }

    @Test
    void consultar_InvalidUserException() {
        ConsultaPontosDto dto = new ConsultaPontosDto("user", LocalDate.now(), LocalDate.now());
        when(pontoService.consultar(dto)).thenThrow(new InvalidUserException("invalid user"));

        ResponseEntity<?> response = pontoController.consultar(dto);

        assertEquals(HttpStatusCode.valueOf(401), response.getStatusCode());
        verify(pontoService, times(1)).consultar(dto);
    }

    @Test
    void consultar_GenericException() {
        ConsultaPontosDto dto = new ConsultaPontosDto("user", LocalDate.now(), LocalDate.now());
        when(pontoService.consultar(dto)).thenThrow(new RuntimeException("unexpected"));

        ResponseEntity<?> response = pontoController.consultar(dto);

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        verify(pontoService, times(1)).consultar(dto);
    }

    @Test
    void alterarPonto_Success() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        when(pontoService.alterarPonto(dto)).thenReturn(null);

        ResponseEntity<?> response = pontoController.alterarPonto(dto);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(pontoService, times(1)).alterarPonto(dto);
    }

    @Test
    void alterarPonto_NotFoundException() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        when(pontoService.alterarPonto(dto)).thenThrow(new NotFoundException("not found"));

        ResponseEntity<?> response = pontoController.alterarPonto(dto);

        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        verify(pontoService, times(1)).alterarPonto(dto);
    }

    @Test
    void alterarPonto_IllegalArgumentException() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        when(pontoService.alterarPonto(dto)).thenThrow(new IllegalArgumentException("bad arg"));

        ResponseEntity<?> response = pontoController.alterarPonto(dto);

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        verify(pontoService, times(1)).alterarPonto(dto);
    }

    @Test
    void alterarPonto_InvalidUserException() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        when(pontoService.alterarPonto(dto)).thenThrow(new InvalidUserException("invalid"));

        ResponseEntity<?> response = pontoController.alterarPonto(dto);

        assertEquals(HttpStatusCode.valueOf(401), response.getStatusCode());
        verify(pontoService, times(1)).alterarPonto(dto);
    }

    @Test
    void alterarPonto_DateTimeException() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        when(pontoService.alterarPonto(dto)).thenThrow(new DateTimeException("bad date"));

        ResponseEntity<?> response = pontoController.alterarPonto(dto);

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        verify(pontoService, times(1)).alterarPonto(dto);
    }

    @Test
    void alterarPonto_GenericException() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        when(pontoService.alterarPonto(dto)).thenThrow(new RuntimeException("fail"));

        ResponseEntity<?> response = pontoController.alterarPonto(dto);

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        verify(pontoService, times(1)).alterarPonto(dto);
    }
}