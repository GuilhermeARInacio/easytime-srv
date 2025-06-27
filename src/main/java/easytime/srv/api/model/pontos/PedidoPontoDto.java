package easytime.srv.api.model.pontos;

import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.util.DateTimeUtil;

import java.time.LocalDate;

public record PedidoPontoDto(
        Integer id,
        String login,
        Integer idPonto,
        LocalDate dataRegistro,
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
          pedidoPonto.getPonto().getData(),
          pedidoPonto.getPonto().getStatusRegistro().name(),
          pedidoPonto.getStatusPedido().name(),
          pedidoPonto.getTipoPedido().name(),
          pedidoPonto.getGestorLogin(),
          DateTimeUtil.convertDBDateTimeToUserDateTime(pedidoPonto.getDataAprovacao()),
          pedidoPonto.getAlteracaoPonto() != null ? pedidoPonto.getAlteracaoPonto().getJustificativa() : "Esse pedido é um registro."
        );
    }
}
