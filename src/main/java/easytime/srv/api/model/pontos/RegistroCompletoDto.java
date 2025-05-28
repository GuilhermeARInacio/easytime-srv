package easytime.srv.api.model.pontos;

import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.util.DateUtil;

import java.sql.Time;
import java.time.LocalDate;

public record RegistroCompletoDto (
        Integer id,
        String login,
        String data,
        Time horasTrabalhadas,
        Time entrada1,
        Time saida1,
        Time entrada2,
        Time saida2,
        Time entrada3,
        Time saida3,
        TimeLog.Status status
){

    public RegistroCompletoDto(TimeLog timeLog){
        this(
                timeLog.getId(),
                timeLog.getUser().getLogin(),
                DateUtil.convertDBDateToUserDate(timeLog.getData()),
                timeLog.getHoras_trabalhadas(), // Pass Duration directly
                timeLog.getE1(),
                timeLog.getS1(),
                timeLog.getE2(),
                timeLog.getS2(),
                timeLog.getE3(),
                timeLog.getS3(),
                timeLog.getStatus()
        );
    }
}
