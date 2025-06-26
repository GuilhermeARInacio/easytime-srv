package easytime.srv.api.model.pontos;

import easytime.srv.api.model.Status;
import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.util.DateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Time;


public record TimeLogDto(
        @Schema(description = "Login do usuário", example = "mkenzo")
        String login,
        @Schema(description = "Data do ponto", example = "01/10/2023")
        String data,
        @Schema(description = "Horário do ponto", example = "08:00:00")
        Time horarioBatida,
        @Schema(description = "Status do ponto", example = "PENDENTE")
        Status status
) {

    public TimeLogDto(PedidoPonto pedido){
        this(
                pedido.getPonto().getUser().getLogin(),
                DateTimeUtil.convertDBDateToUserDate(pedido.getPonto().getData()),
                (Time) pedido.getPonto().getUltimoBatimentoValue(),
                pedido.getPonto().getStatusRegistro()
        );
    }
}
