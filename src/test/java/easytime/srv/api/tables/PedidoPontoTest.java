// File: src/test/java/easytime/srv/api/tables/PedidoPontoTest.java
package easytime.srv.api.tables;

import easytime.srv.api.model.pontos.AlterarPonto;
import easytime.srv.api.model.pontos.AlterarPontoDto;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoPontoTest {

    @Test
    void constructorWithAlterarPontoDto_setsFieldsCorrectly() {
        User user = new User();
        TimeLog ponto = mock(TimeLog.class);
        AlterarPontoDto dto = mock(AlterarPontoDto.class);

        PedidoPonto pedido = new PedidoPonto(dto, ponto, user);

        assertEquals(user, pedido.getUser());
        assertEquals(ponto, pedido.getPonto());
        assertEquals(PedidoPonto.Tipo.ALTERACAO, pedido.getTipoPedido());
        assertNotNull(pedido.getHorarioCriacao());
        assertNotNull(pedido.getAlteracaoPonto());
        assertEquals(TimeLog.Status.PENDENTE, pedido.getPonto().getStatus());
    }

    @Test
    void constructorWithTimeLog_setsFieldsCorrectly() {
        User user = new User();
        TimeLog ponto = mock(TimeLog.class);
        when(ponto.getUser()).thenReturn(user);

        PedidoPonto pedido = new PedidoPonto(ponto);

        assertEquals(user, pedido.getUser());
        assertEquals(ponto, pedido.getPonto());
        assertEquals(PedidoPonto.Tipo.REGISTRO, pedido.getTipoPedido());
        assertNotNull(pedido.getHorarioCriacao());
        assertNull(pedido.getAlteracaoPonto());
        assertEquals(TimeLog.Status.PENDENTE, pedido.getPonto().getStatus());
    }

    @Test
    void settersAndGetters_workAsExpected() {
        PedidoPonto pedido = new PedidoPonto();
        pedido.setGestorLogin("gestor");
        LocalDateTime now = LocalDateTime.now();
        pedido.setDataAprovacao(now);
        pedido.getPonto().setStatus(TimeLog.Status.APROVADO);

        assertEquals("gestor", pedido.getGestorLogin());
        assertEquals(now, pedido.getDataAprovacao());
        assertEquals(TimeLog.Status.APROVADO, pedido.getPonto().getStatus());
    }

    @Test
    void enums_haveExpectedValues() {
        assertEquals("ALTERACAO", PedidoPonto.Tipo.ALTERACAO.name());
        assertEquals("REGISTRO", PedidoPonto.Tipo.REGISTRO.name());
        assertEquals("PENDENTE", TimeLog.Status.PENDENTE.name());
        assertEquals("APROVADO", TimeLog.Status.APROVADO.name());
        assertEquals("REPROVADO", TimeLog.Status.REPROVADO.name());
    }
}