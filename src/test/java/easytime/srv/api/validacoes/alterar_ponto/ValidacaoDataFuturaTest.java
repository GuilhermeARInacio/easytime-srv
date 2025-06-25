// File: src/test/java/easytime/srv/api/validacoes/ponto/alterar_ponto/ValidacaoDataFuturaTest.java
package easytime.srv.api.validacoes.alterar_ponto;

import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.validacoes.ponto.alterar_ponto.ValidacaoDataFutura;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ValidacaoDataFuturaTest {

    private final ValidacaoDataFutura validacao = new ValidacaoDataFutura();
    private final AlterarPontoDto alterarPontoDto = new AlterarPontoDto(
            "user",
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
    @Test
    void validar_dataHoje_naoLancaExcecao() {
        TimeLog timeLog = mock(TimeLog.class);

        assertDoesNotThrow(() -> validacao.validar(alterarPontoDto, timeLog, "user"));
    }

    @Test
    void validar_dataPassada_naoLancaExcecao() {

        TimeLog timeLog = mock(TimeLog.class);

        assertDoesNotThrow(() -> validacao.validar(alterarPontoDto, timeLog, "user"));
    }

    @Test
    void validar_dataFutura_lancaExcecao() {
        LocalDate futureDate = LocalDate.now().plusDays(7);
        String formattedDate = futureDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        AlterarPontoDto dto = new AlterarPontoDto(
                "user",
                1,
                formattedDate,
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                LocalTime.of(17, 0),
                null,
                null,
                "Justificativa"
        );

        TimeLog timeLog = mock(TimeLog.class);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validacao.validar(dto, timeLog, "user"));
        assertEquals("Data não pode ser futura.", ex.getMessage());
    }
}