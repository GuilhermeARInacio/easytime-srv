// File: src/test/java/easytime/srv/api/validacoes/ponto/alterar_ponto/ValidacaoLoginNaoPertenceTest.java
package easytime.srv.api.validacoes.alterar_ponto;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.tables.User;
import easytime.srv.api.tables.repositorys.UserRepository;
import easytime.srv.api.validacoes.ponto.alterar_ponto.ValidacaoLoginNaoPertence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidacaoLoginNaoPertenceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ValidacaoLoginNaoPertence validacao;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        validacao = new ValidacaoLoginNaoPertence();
        // Inject userRepository mock via reflection
        Field field = ValidacaoLoginNaoPertence.class.getDeclaredField("userRepository");
        field.setAccessible(true);
        field.set(validacao, userRepository);
    }

    private AlterarPontoDto getDto() {
        return new AlterarPontoDto(
                "login",
                1,
                "01/06/2024",
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                LocalTime.of(17, 0),
                null,
                null,
                "Justificativa"
        );
    }

    @Test
    void validar_usuarioNaoEncontrado_lancaInvalidUserException() {
        when(userRepository.findByLogin("user1")).thenReturn(Optional.empty());
        TimeLog timeLog = mock(TimeLog.class);

        assertThrows(InvalidUserException.class, () ->
                validacao.validar(getDto(), timeLog, "user1"));
    }

    @Test
    void validar_usuarioAdmin_naoLancaExcecao() {
        User admin = mock(User.class);
        when(admin.getRole()).thenReturn("admin");
        when(userRepository.findByLogin("admin")).thenReturn(Optional.of(admin));
        TimeLog timeLog = mock(TimeLog.class);

        assertDoesNotThrow(() -> validacao.validar(getDto(), timeLog, "admin"));
    }

    @Test
    void validar_usuarioDonoDoPonto_naoLancaExcecao() {
        User user = mock(User.class);
        when(user.getRole()).thenReturn("user");
        when(userRepository.findByLogin("user1")).thenReturn(Optional.of(user));

        User dono = mock(User.class);
        when(dono.getLogin()).thenReturn("user1");
        TimeLog timeLog = mock(TimeLog.class);
        when(timeLog.getUser()).thenReturn(dono);

        assertDoesNotThrow(() -> validacao.validar(getDto(), timeLog, "user1"));
    }

    @Test
    void validar_usuarioNaoDonoENaoAdmin_lancaIllegalCallerException() {
        User user = mock(User.class);
        when(user.getRole()).thenReturn("user");
        when(userRepository.findByLogin("user2")).thenReturn(Optional.of(user));

        User dono = mock(User.class);
        when(dono.getLogin()).thenReturn("user1");
        TimeLog timeLog = mock(TimeLog.class);
        when(timeLog.getUser()).thenReturn(dono);

        assertThrows(IllegalCallerException.class, () ->
                validacao.validar(getDto(), timeLog, "user2"));
    }
}