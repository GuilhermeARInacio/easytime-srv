// File: src/test/java/easytime/srv/api/validacoes/ponto/alterar_ponto/ValidacaoPedidoAlterarAlreadyExistsTest.java
package easytime.srv.api.validacoes.ponto.alterar_ponto;

import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.tables.repositorys.PedidoPontoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidacaoPedidoAlterarAlreadyExistsTest {

    @Mock
    private PedidoPontoRepository pedidoPontoRepository;

    @InjectMocks
    private ValidacaoPedidoAlterarAlreadyExists validacao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private AlterarPontoDto getDto() {
        return new AlterarPontoDto(
                1, "01/06/2024",
                LocalTime.of(8, 0), LocalTime.of(12, 0),
                LocalTime.of(13, 0), LocalTime.of(17, 0),
                null, null
        );
    }

    @Test
    void validar_pedidoNaoExiste_naoLancaExcecao() {
        when(pedidoPontoRepository.existsByPonto_IdAndTipoPedido(1, PedidoPonto.Tipo.ALTERACAO)).thenReturn(false);
        TimeLog timeLog = mock(TimeLog.class);

        assertDoesNotThrow(() -> validacao.validar(getDto(), timeLog, "user"));
    }

    @Test
    void validar_pedidoJaExiste_lancaIllegalArgumentException() {
        when(pedidoPontoRepository.existsByPonto_IdAndTipoPedido(1, PedidoPonto.Tipo.ALTERACAO)).thenReturn(true);
        TimeLog timeLog = mock(TimeLog.class);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validacao.validar(getDto(), timeLog, "user"));
        assertEquals("Já existe um pedido de alteração para este ponto.", ex.getMessage());
    }
}