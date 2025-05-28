package easytime.srv.api.model.pontos;

import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.util.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Time;
import java.time.LocalDate;


public record TimeLogDto(
        @Schema(description = "Login do usuário", example = "mkenzo")
        String login,
        @Schema(description = "Data do ponto", example = "01/10/2023")
        String data,
        @Schema(description = "Horário do ponto", example = "08:00:00")
        Time horarioBatida,
        @Schema(description = "Status do ponto", example = "PENDENTE")
        TimeLog.Status status
) {

    public TimeLogDto(TimeLog timeLog){
        this(
                timeLog.getUser().getLogin(),
                DateUtil.convertDBDateToUserDate(timeLog.getData()),
                (Time) timeLog.getUltimoBatimentoValue(),
                timeLog.getStatus()
        );
    }
}
