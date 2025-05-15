package easytime.srv.api.controller;

import easytime.srv.api.model.pontos.TimeLogDto;
import easytime.srv.api.model.user.LoginDto;
import easytime.srv.api.service.PontoService;
import easytime.srv.api.tables.TimeLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.webjars.NotFoundException;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

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

        when(pontoService.registrarPonto(login, dataHoje, horaAgora)).thenReturn(timeLogDto);

        // Act
        ResponseEntity<?> response = pontoController.registrarPonto(login);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(timeLogDto, response.getBody());
        verify(pontoService, times(1)).registrarPonto(login, dataHoje, horaAgora);
    }

    @Test
    void registrarPonto_NotFoundException() {
        // Arrange
        LoginDto login = new LoginDto("invalid");
        when(pontoService.registrarPonto(any(), any(), any())).thenThrow(new NotFoundException("Usuário não encontrado."));

        // Act
        ResponseEntity<?> response = pontoController.registrarPonto(login);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Usuário não encontrado."));
        verify(pontoService, times(1)).registrarPonto(any(), any(), any());
    }

    @Test
    void registrarPonto_IllegalArgumentException() {
        // Arrange
        LoginDto login = new LoginDto("mkenzo");
        when(pontoService.registrarPonto(any(), any(), any())).thenThrow(new IllegalArgumentException("Erro de validação."));

        // Act
        ResponseEntity<?> response = pontoController.registrarPonto(login);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Erro de validação."));
        verify(pontoService, times(1)).registrarPonto(any(), any(), any());
    }

    @Test
    void registrarPonto_InternalServerError() {
        // Arrange
        LoginDto login = new LoginDto("mkenzo");
        when(pontoService.registrarPonto(any(), any(), any())).thenThrow(new RuntimeException("Erro inesperado."));

        // Act
        ResponseEntity<?> response = pontoController.registrarPonto(login);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Erro inesperado."));
        verify(pontoService, times(1)).registrarPonto(any(), any(), any());
    }

    @Test
    void removerPonto_Success() {
        // Arrange
        Integer id = 1;
        doNothing().when(pontoService).removerPonto(id);

        // Act
        ResponseEntity<?> response = pontoController.removerPonto(id);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
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
        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Ponto não encontrado."));
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
        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Erro inesperado."));
        verify(pontoService, times(1)).removerPonto(id);
    }
}