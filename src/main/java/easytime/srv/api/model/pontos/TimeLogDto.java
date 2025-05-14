package easytime.srv.api.model.pontos;

import easytime.srv.api.tables.TimeLog;

import java.sql.Time;
import java.time.LocalDate;

public record TimeLogDto(
        String login,
        LocalDate data,
        Time horarioBatida,
        TimeLog.Status status
) {

    public TimeLogDto(TimeLog timeLog){
        this(
                timeLog.getUser().getLogin(),
                timeLog.getData(),
                (Time) timeLog.getUltimoBatimentoValue(),
                timeLog.getStatus()
        );
    }
}
