package easytime.srv.api.model.pontos;

import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.util.DateTimeUtil;

import java.time.LocalDate;

public record PedidoPontoDto(
        Integer id,
        String login,
        Integer idPonto,
        String dataRegistro,
        String dataPedido,
        String statusRegistro,
        AlterarPonto alteracaoPonto
//        String statusPedido
//        String tipo_pedido,
//        String gestorLogin,
//        String dataAprovacao,
//        String justificativa
) {
    public PedidoPontoDto(PedidoPonto pedidoPonto) {
        this(
          pedidoPonto.getId(),
          pedidoPonto.getUser().getLogin(),
          pedidoPonto.getPonto().getId(),
          DateTimeUtil.convertDBDateToUserDate(pedidoPonto.getPonto().getData()),
          DateTimeUtil.convertDBDateToUserDate(pedidoPonto.getHorarioCriacao().toLocalDate()),
          pedidoPonto.getPonto().getStatusRegistro().name(),
          pedidoPonto.getAlteracaoPonto() == null ? null : pedidoPonto.getAlteracaoPonto()
        );
    }
}
