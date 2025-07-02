package easytime.srv.api.model.pontos;

import easytime.srv.api.model.Status;
import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.util.DateTimeUtil;

import java.time.LocalTime;

public record AlterarPontoDto(
        String login,
        Integer idPonto,
        LocalTime entrada1,
        LocalTime saida1,
        LocalTime entrada2,
        LocalTime saida2,
        LocalTime entrada3,
        LocalTime saida3,
        String justificativa,
        Status status
) {
    public AlterarPontoDto(PedidoPonto pedido){
        this(
                pedido.getUser().getLogin(),
                pedido.getPonto().getId(),
                pedido.getAlteracaoPonto().getEntrada1(),
                pedido.getAlteracaoPonto().getSaida1(),
                pedido.getAlteracaoPonto().getEntrada2(),
                pedido.getAlteracaoPonto().getSaida2(),
                pedido.getAlteracaoPonto().getEntrada3(),
                pedido.getAlteracaoPonto().getSaida3(),
                pedido.getAlteracaoPonto().getJustificativa(),
                pedido.getStatusPedido()
        );
    }
}
