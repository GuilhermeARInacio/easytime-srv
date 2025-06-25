// File: src/test/java/easytime/srv/api/validacoes/ponto/alterar_ponto/ValidacaoHorarioInvalidoTest.java
package easytime.srv.api.validacoes.alterar_ponto;

import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.validacoes.ponto.alterar_ponto.ValidacaoHorarioInvalido;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ValidacaoHorarioInvalidoTest {

    private final ValidacaoHorarioInvalido validacao = new ValidacaoHorarioInvalido();

    @Test
    void validar_todosHorariosValidos_naoLancaExcecao() {
        AlterarPontoDto dto = new AlterarPontoDto(
                "login",
                1,
                "01/06/2024",
                LocalTime.of(6, 0),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                LocalTime.of(18, 0),
                null,
                null,
                "Justificativa"
        );
        TimeLog timeLog = mock(TimeLog.class);

        assertDoesNotThrow(() -> validacao.validar(dto, timeLog, "user"));
    }

    @Test
    void validar_horarioAntesDas6_lancaExcecao() {
        AlterarPontoDto dto = new AlterarPontoDto(
                "Login",
                1,
                "01/06/2024",
                LocalTime.of(5, 59),
                null,
                null,
                null,
                null,
                null,
                "Justificativa"
        );
        TimeLog timeLog = mock(TimeLog.class);

        assertThrows(IllegalArgumentException.class,
                () -> validacao.validar(dto, timeLog, "user"));
    }

    @Test
    void validar_horarioDepoisDas23_lancaExcecao() {
        AlterarPontoDto dto = new AlterarPontoDto(
                "login",
                1,
                "01/06/2024",
                LocalTime.of(23, 1),
                LocalTime.of(23, 1),
                null,
                null,
                null,
                null,
                "justificativa"
        );
        TimeLog timeLog = mock(TimeLog.class);

        assertThrows(IllegalArgumentException.class,
                () -> validacao.validar(dto, timeLog, "user"));
    }

    @Test
    void validar_todosHorariosNulos_naoLancaExcecao() {
        AlterarPontoDto dto = new AlterarPontoDto(
                "login",
                1,
                "01/06/2024",
                null,
                null,
                null,
                null,
                null,
                null,
                "justificativa"
        );
        TimeLog timeLog = mock(TimeLog.class);

        assertDoesNotThrow(() -> validacao.validar(dto, timeLog, "user"));
    }
}