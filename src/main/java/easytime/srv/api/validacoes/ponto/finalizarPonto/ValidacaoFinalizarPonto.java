package easytime.srv.api.validacoes.ponto.finalizarPonto;

import easytime.srv.api.tables.PedidoPonto;

public interface ValidacaoFinalizarPonto {
    void validar(PedidoPonto pedido, String userLogin);
}
