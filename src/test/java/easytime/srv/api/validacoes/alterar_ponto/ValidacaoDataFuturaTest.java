// src/test/java/easytime/srv/api/validacoes/alterar_ponto/ValidacaoDataFuturaTest.java
package easytime.srv.api.validacoes.alterar_ponto;

import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.tables.TimeLog;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ValidacaoDataFuturaTest {

    private final ValidacaoDataFutura validacao = new ValidacaoDataFutura();

    @Test
    void validar_dataHoje_naoLancaExcecao() {
        String today = LocalDate.now().toString();
        AlterarPontoDto dto = new AlterarPontoDto(
                "user", 1, today,
                LocalTime.of(8,0), null, null, null, null, null
        );
        TimeLog timeLog = mock(TimeLog.class);

        assertDoesNotThrow(() -> validacao.validar(dto, timeLog));
    }

    @Test
    void validar_dataPassada_naoLancaExcecao() {
        String past = LocalDate.now().minusDays(1).toString();
        AlterarPontoDto dto = new AlterarPontoDto(
                "user", 1, past,
                LocalTime.of(8,0), null, null, null, null, null
        );
        TimeLog timeLog = mock(TimeLog.class);

        assertDoesNotThrow(() -> validacao.validar(dto, timeLog));
    }

    @Test
    void validar_dataFutura_lancaExcecao() {
        String future = LocalDate.now().plusDays(1).toString();
        AlterarPontoDto dto = new AlterarPontoDto(
                "user", 1, future,
                LocalTime.of(8,0), null, null, null, null, null
        );
        TimeLog timeLog = mock(TimeLog.class);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validacao.validar(dto, timeLog));
        assertEquals("Data não pode ser futura.", ex.getMessage());
    }
}