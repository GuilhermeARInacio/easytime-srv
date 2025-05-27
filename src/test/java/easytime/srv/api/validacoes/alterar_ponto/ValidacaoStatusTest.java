// src/test/java/easytime/srv/api/validacoes/alterar_ponto/ValidacaoStatusTest.java
package easytime.srv.api.validacoes.alterar_ponto;

import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.tables.TimeLog;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidacaoStatusTest {

    private final ValidacaoStatus validacao = new ValidacaoStatus();

    @Test
    void validar_statusAprovado_lancaExcecao() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        TimeLog timeLog = mock(TimeLog.class);
        when(timeLog.getStatus()).thenReturn(TimeLog.Status.APROVADO);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validacao.validar(dto, timeLog));
        assertEquals("O ponto já foi aprovado, não tem como ser alterado.", ex.getMessage());
    }

    @Test
    void validar_statusNaoAprovado_naoLancaExcecao() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        TimeLog timeLog = mock(TimeLog.class);
        when(timeLog.getStatus()).thenReturn(TimeLog.Status.PENDENTE);

        assertDoesNotThrow(() -> validacao.validar(dto, timeLog));
    }
}