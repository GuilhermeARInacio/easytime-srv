package easytime.srv.api.model.pontos;

import com.fasterxml.jackson.annotation.JsonInclude;
import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.util.DateTimeUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PedidoPontoDto(
        Integer id,
        String login,
        Integer idPonto,
        String status,
        String tipo_pedido,
        String gestorLogin,
        String dataAprovacao
) {
    public PedidoPontoDto(PedidoPonto pedidoPonto) {
        this(
          pedidoPonto.getId(),
          pedidoPonto.getUser().getLogin(),
          pedidoPonto.getPonto().getId(),
          pedidoPonto.getStatus().name(),
          pedidoPonto.getTipoPedido().name(),
                pedidoPonto.getGestorLogin(),
                DateTimeUtil.convertDBDateTimeToUserDateTime(pedidoPonto.getDataAprovacao())
        );
    }
}
