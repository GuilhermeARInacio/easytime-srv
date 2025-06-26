package easytime.srv.api.validacoes.ponto.finalizarPonto;

import easytime.srv.api.model.Status;
import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.tables.TimeLog;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoPedidoStatus implements ValidacaoFinalizarPonto{
    @Override
    public void validar(PedidoPonto pedido, String userLogin) {
        if(pedido.getStatusPedido() == Status.APROVADO ||
           pedido.getStatusPedido() == Status.REJEITADO) {
            throw new IllegalArgumentException("Pedido já finalizado.");
        }
    }
}
