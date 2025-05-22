package easytime.srv.api.service;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.model.pontos.ConsultaPontosDto;
import easytime.srv.api.model.pontos.RegistroCompletoDto;
import easytime.srv.api.model.pontos.TimeLogDto;
import easytime.srv.api.model.user.LoginDto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.tables.User;
import easytime.srv.api.tables.repositorys.TimeLogsRepository;
import easytime.srv.api.tables.repositorys.UserRepository;
import easytime.srv.api.validacoes.alterar_ponto.ValidacaoAlterarPonto;
import easytime.srv.api.validacoes.bater_ponto.ValidacaoPonto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.webjars.NotFoundException;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Collections;
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
    private List<ValidacaoAlterarPonto> validacoesAlterar;

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

    @Test
    void consultar_Success() {
        // Arrange
        String login = "mkenzo";
        LocalDate dtInicio = LocalDate.now().minusDays(1);
        LocalDate dtFinal = LocalDate.now();
        User user = new User();
        user.setLogin(login);
        TimeLog timeLog = new TimeLog(user, dtInicio);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
        when(timeLogsRepository.findAllByUserAndDataBetween(user, dtInicio, dtFinal)).thenReturn(List.of(timeLog));

        // Act
        List<RegistroCompletoDto> result = pontoService.consultar(new ConsultaPontosDto(login, dtInicio, dtFinal));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void consultar_UserNotFound() {
        // Arrange
        String login = "invalid";
        LocalDate dtInicio = LocalDate.now().minusDays(1);
        LocalDate dtFinal = LocalDate.now();

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        // Act & Assert
        InvalidUserException exception = assertThrows(InvalidUserException.class, () ->
                pontoService.consultar(new ConsultaPontosDto(login, dtInicio, dtFinal))
        );
        assertEquals("Usuário não encontrado.", exception.getMessage());
    }

    @Test
    void consultar_IllegalArgument() {
        // Arrange
        String login = "mkenzo";
        LocalDate dtInicio = LocalDate.of(2025, 5, 10);
        LocalDate dtFinal = LocalDate.of(2025, 5, 2);
        User user = new User();
        user.setLogin(login);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                pontoService.consultar(new ConsultaPontosDto(login, dtInicio, dtFinal))
        );
        assertEquals("A data de início não pode ser posterior à data final.", exception.getMessage());
    }

    @Test
    void consultar_responseEmpty() {
        // Arrange
        String login = "mkenzo";
        LocalDate dtInicio = LocalDate.now().minusDays(1);
        LocalDate dtFinal = LocalDate.now();
        User user = new User();
        user.setLogin(login);
        TimeLog timeLog = new TimeLog(user, dtInicio);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
        when(timeLogsRepository.findAllByUserAndDataBetween(user, dtInicio, dtFinal)).thenReturn(Collections.emptyList());

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                pontoService.consultar(new ConsultaPontosDto(login, dtInicio, dtFinal)));
        assertEquals("Nenhum ponto encontrado.", exception.getMessage());
    }

    @Test
    void alterarPonto_Success() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        when(dto.idPonto()).thenReturn(1);
        when(dto.login()).thenReturn("user");
        TimeLog timeLog = mock(TimeLog.class);
        User user = new User();
        when(timeLog.getUser()).thenReturn(user);
        when(timeLogsRepository.findById(1)).thenReturn(Optional.of(timeLog));
        when(userRepository.findByLogin("user")).thenReturn(Optional.of(user));

        RegistroCompletoDto result = pontoService.alterarPonto(dto);

        assertNotNull(result);
    }

    @Test
    void alterarPonto_TimeLogNotFound() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        when(dto.idPonto()).thenReturn(1);
        when(timeLogsRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> pontoService.alterarPonto(dto));
        assertEquals("Ponto não encontrado.", ex.getMessage());
        verify(timeLogsRepository, never()).save(any());
    }

    @Test
    void alterarPonto_UserNotFound() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        when(dto.idPonto()).thenReturn(1);
        when(dto.login()).thenReturn("user");
        TimeLog timeLog = mock(TimeLog.class);
        when(timeLogsRepository.findById(1)).thenReturn(Optional.of(timeLog));
        when(userRepository.findByLogin("user")).thenReturn(Optional.empty());

        InvalidUserException ex = assertThrows(InvalidUserException.class, () -> pontoService.alterarPonto(dto));
        assertEquals("Login inválido.", ex.getMessage());
        verify(timeLogsRepository, never()).save(any());
    }
}