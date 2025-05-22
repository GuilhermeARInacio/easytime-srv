// src/test/java/easytime/srv/api/validacoes/alterar_ponto/ValidacaoHorarioInvalidoTest.java
package easytime.srv.api.validacoes.alterar_ponto;

import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.tables.TimeLog;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ValidacaoHorarioInvalidoTest {

    private final ValidacaoHorarioInvalido validacao = new ValidacaoHorarioInvalido();

    @Test
    void validar_todosHorariosValidos_naoLancaExcecao() {
        AlterarPontoDto dto = new AlterarPontoDto(
                "user", 1, "2024-06-01",
                LocalTime.of(6, 0), LocalTime.of(10, 0),
                LocalTime.of(12, 0), LocalTime.of(18, 0),
                LocalTime.of(20, 0), LocalTime.of(23, 0)
        );
        TimeLog timeLog = mock(TimeLog.class);

        assertDoesNotThrow(() -> validacao.validar(dto, timeLog));
    }

    @Test
    void validar_horarioAntesDas6_lancaExcecao() {
        AlterarPontoDto dto = new AlterarPontoDto(
                "user", 1, "2024-06-01",
                LocalTime.of(5, 59), null, null, null, null, null
        );
        TimeLog timeLog = mock(TimeLog.class);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validacao.validar(dto, timeLog));
        assertEquals("Horários entre 23h e 6h não são permitidos.", ex.getMessage());
    }

    @Test
    void validar_horarioDepoisDas23_lancaExcecao() {
        AlterarPontoDto dto = new AlterarPontoDto(
                "user", 1, "2024-06-01",
                null, null, null, null, null, LocalTime.of(23, 1)
        );
        TimeLog timeLog = mock(TimeLog.class);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validacao.validar(dto, timeLog));
        assertEquals("Horários entre 23h e 6h não são permitidos.", ex.getMessage());
    }

    @Test
    void validar_camposNulos_naoLancaExcecao() {
        AlterarPontoDto dto = new AlterarPontoDto(
                "user", 1, "2024-06-01",
                null, null, null, null, null, null
        );
        TimeLog timeLog = mock(TimeLog.class);

        assertDoesNotThrow(() -> validacao.validar(dto, timeLog));
    }


}