package easytime.srv.api.service;

import easytime.srv.api.model.pontos.TimeLogDto;
import easytime.srv.api.model.user.LoginDto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.tables.User;
import easytime.srv.api.tables.repositorys.TimeLogsRepository;
import easytime.srv.api.tables.repositorys.UserRepository;
import easytime.srv.api.validacoes.bater_ponto.ValidacaoPonto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.webjars.NotFoundException;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PontoServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TimeLogsRepository timeLogsRepository;

    @Mock
    private List<ValidacaoPonto> validacoes;

    @InjectMocks
    private PontoService pontoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarPonto_Success() {
        // Arrange
        LoginDto login = new LoginDto("mkenzo");
        LocalDate dataHoje = LocalDate.now();
        Time horaAgora = Time.valueOf("08:00:00");
        User user = new User();
        user.setLogin("mkenzo");
        TimeLog timeLog = new TimeLog(user, dataHoje);

        when(userRepository.findByLogin(login.login())).thenReturn(Optional.of(user));
        when(timeLogsRepository.findByUserAndData(user, dataHoje)).thenReturn(Optional.of(timeLog));

        // Act
        TimeLogDto result = pontoService.registrarPonto(login);

        // Assert
        assertNotNull(result);
        verify(validacoes, times(1)).forEach(any());
        verify(timeLogsRepository, times(1)).save(timeLog);
    }

    @Test
    void registrarPonto_UserNotFound() {
        // Arrange
        LoginDto login = new LoginDto("invalid");
        LocalDate dataHoje = LocalDate.now();
        Time horaAgora = Time.valueOf("08:00:00");

        when(userRepository.findByLogin(login.login())).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                pontoService.registrarPonto(login)
        );
        assertEquals("Usuário não encontrado.", exception.getMessage());
        verify(timeLogsRepository, never()).save(any());
    }

    @Test
    void registrarPonto_NewTimeLog() {
        // Arrange
        LoginDto login = new LoginDto("mkenzo");
        LocalDate dataHoje = LocalDate.now();
        Time horaAgora = Time.valueOf("08:00:00");
        User user = new User();
        user.setLogin("mkenzo");

        when(userRepository.findByLogin(login.login())).thenReturn(Optional.of(user));
        when(timeLogsRepository.findByUserAndData(user, dataHoje)).thenReturn(Optional.empty());

        // Act
        TimeLogDto result = pontoService.registrarPonto(login);

        // Assert
        assertNotNull(result);
        verify(validacoes, times(1)).forEach(any());
        verify(timeLogsRepository, times(1)).save(any(TimeLog.class));
    }

    @Test
    void removerPonto_Success() {
        // Arrange
        Integer id = 1;
        TimeLog timeLog = new TimeLog();
        when(timeLogsRepository.findById(id)).thenReturn(Optional.of(timeLog));

        // Act
        pontoService.removerPonto(id);

        // Assert
        verify(timeLogsRepository, times(1)).delete(timeLog);
    }

    @Test
    void removerPonto_NotFound() {
        // Arrange
        Integer id = 1;
        when(timeLogsRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                pontoService.removerPonto(id)
        );
        assertEquals("Ponto não encontrado.", exception.getMessage());
        verify(timeLogsRepository, never()).delete(any());
    }
}