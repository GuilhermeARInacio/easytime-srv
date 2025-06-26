package easytime.srv.api.model.pontos;

import com.fasterxml.jackson.annotation.JsonInclude;
import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.util.DateTimeUtil;

public record PedidoPontoDto(
        Integer id,
        String login,
        Integer idPonto,
        String statusRegistro,
        String statusPedido,
        String tipo_pedido,
        String gestorLogin,
        String dataAprovacao,
        String justificativa
) {
    public PedidoPontoDto(PedidoPonto pedidoPonto) {
        this(
          pedidoPonto.getId(),
          pedidoPonto.getUser().getLogin(),
          pedidoPonto.getPonto().getId(),
          pedidoPonto.getPonto().getStatusRegistro().name(),
          pedidoPonto.getStatusPedido().name(),
          pedidoPonto.getTipoPedido().name(),
          pedidoPonto.getGestorLogin(),
          DateTimeUtil.convertDBDateTimeToUserDateTime(pedidoPonto.getDataAprovacao()),
          pedidoPonto.getAlteracaoPonto().getJustificativa()
        );
    }
}
