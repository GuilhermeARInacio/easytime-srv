// src/test/java/easytime/srv/api/validacoes/alterar_ponto/ValidacaoLoginNaoPertenceTest.java
package easytime.srv.api.validacoes.alterar_ponto;

import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.tables.User;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidacaoLoginNaoPertenceTest {

    private final ValidacaoLoginNaoPertence validacao = new ValidacaoLoginNaoPertence();

    @Test
    void validar_loginPertence_naoLancaExcecao() {
        AlterarPontoDto dto = new AlterarPontoDto(
                "user1", 1, "2024-06-01",
                LocalTime.of(8,0), null, null, null, null, null
        );
        User user = mock(User.class);
        when(user.getLogin()).thenReturn("user1");
        TimeLog timeLog = mock(TimeLog.class);
        when(timeLog.getUser()).thenReturn(user);

        assertDoesNotThrow(() -> validacao.validar(dto, timeLog));
    }

    @Test
    void validar_loginNaoPertence_lancaExcecao() {
        AlterarPontoDto dto = new AlterarPontoDto(
                "user2", 1, "2024-06-01",
                LocalTime.of(8,0), null, null, null, null, null
        );
        User user = mock(User.class);
        when(user.getLogin()).thenReturn("user1");
        TimeLog timeLog = mock(TimeLog.class);
        when(timeLog.getUser()).thenReturn(user);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validacao.validar(dto, timeLog));
        assertEquals("O ponto não pertence ao usuário informado.", ex.getMessage());
    }
}