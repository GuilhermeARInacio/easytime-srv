// File: src/test/java/easytime/srv/api/validacoes/ponto/finalizarPonto/ValidacaoPedidoStatusTest.java
package easytime.srv.api.validacoes.ponto.finalizarPonto;

import easytime.srv.api.tables.PedidoPonto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidacaoPedidoStatusTest {

    private ValidacaoPedidoStatus validacao;

    @BeforeEach
    void setUp() {
        validacao = new ValidacaoPedidoStatus();
    }

    @Test
    void validar_statusAprovado_lancaIllegalArgumentException() {
        PedidoPonto pedido = mock(PedidoPonto.class);
        when(pedido.getStatus()).thenReturn(PedidoPonto.Status.APROVADO);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validacao.validar(pedido, "user"));
        assertEquals("Pedido já finalizado.", ex.getMessage());
    }

    @Test
    void validar_statusReprovado_lancaIllegalArgumentException() {
        PedidoPonto pedido = mock(PedidoPonto.class);
        when(pedido.getStatus()).thenReturn(PedidoPonto.Status.REPROVADO);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validacao.validar(pedido, "user"));
        assertEquals("Pedido já finalizado.", ex.getMessage());
    }

    @Test
    void validar_statusPendente_naoLancaExcecao() {
        PedidoPonto pedido = mock(PedidoPonto.class);
        when(pedido.getStatus()).thenReturn(PedidoPonto.Status.PENDENTE);

        assertDoesNotThrow(() -> validacao.validar(pedido, "user"));
    }
}