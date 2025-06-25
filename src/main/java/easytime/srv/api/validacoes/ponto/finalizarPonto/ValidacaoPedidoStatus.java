package easytime.srv.api.validacoes.ponto.finalizarPonto;

import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.tables.TimeLog;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoPedidoStatus implements ValidacaoFinalizarPonto{
    @Override
    public void validar(PedidoPonto pedido, String userLogin) {
        if(pedido.getPonto().getStatus() == TimeLog.Status.APROVADO ||
           pedido.getPonto().getStatus() == TimeLog.Status.REPROVADO) {
            throw new IllegalArgumentException("Pedido já finalizado.");
        }
    }
}
