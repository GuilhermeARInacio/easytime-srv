package easytime.srv.api.validacoes.ponto.finalizarPonto;

import easytime.srv.api.tables.PedidoPonto;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoPedidoStatus implements ValidacaoFinalizarPonto{
    @Override
    public void validar(PedidoPonto pedido, String userLogin) {
        if(pedido.getStatus() == PedidoPonto.Status.APROVADO ||
           pedido.getStatus() == PedidoPonto.Status.REPROVADO) {
            throw new IllegalArgumentException("Pedido já finalizado.");
        }
    }
}
