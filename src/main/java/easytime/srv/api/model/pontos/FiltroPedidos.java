package easytime.srv.api.model.pontos;

import easytime.srv.api.model.Status;
import easytime.srv.api.tables.PedidoPonto;

public record FiltroPedidos(
        String dtInicio,
        String dtFinal,
        Status status,
        PedidoPonto.Tipo tipo
) {
}
