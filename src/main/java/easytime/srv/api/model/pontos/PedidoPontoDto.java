package easytime.srv.api.model.pontos;

import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.util.DateTimeUtil;

import java.time.LocalDate;

public record PedidoPontoDto(
        Integer id,
        String login,
        Integer idPonto,
        String dataRegistro,
        String tipoPedido,
        String dataPedido,
        String statusRegistro,
        String statusPedido,
        AlterarPonto alteracaoPonto
) {
    public PedidoPontoDto(PedidoPonto pedidoPonto) {
        this(
          pedidoPonto.getId(),
          pedidoPonto.getUser().getLogin(),
          pedidoPonto.getPonto().getId(),
          DateTimeUtil.convertDBDateToUserDate(pedidoPonto.getPonto().getData()),
          pedidoPonto.getTipoPedido().name(),
          DateTimeUtil.convertDBDateToUserDate(pedidoPonto.getHorarioCriacao().toLocalDate()),
          pedidoPonto.getPonto().getStatusRegistro().name(),
          pedidoPonto.getStatusPedido().name(),
          pedidoPonto.getAlteracaoPonto() == null ? null : pedidoPonto.getAlteracaoPonto()
        );
    }
}
