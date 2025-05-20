package easytime.srv.api.model.pontos;

import easytime.srv.api.tables.TimeLog;

import java.sql.Time;
import java.time.LocalDate;

public record RegistroCompletoDto (
        Integer id,
        String login,
        LocalDate data,
        Time entrada1,
        Time saida1,
        Time entrada2,
        Time saida2,
        Time entrada3,
        Time saida3,
        Float horas_trabalhadas,
        TimeLog.Status status
){

    public RegistroCompletoDto(TimeLog timeLog){
        this(
                timeLog.getId(),
                timeLog.getUser().getLogin(),
                timeLog.getData(),
                timeLog.getE1(),
                timeLog.getS1(),
                timeLog.getE2(),
                timeLog.getS2(),
                timeLog.getE3(),
                timeLog.getS3(),
                timeLog.getHoras_trabalhadas(),
                timeLog.getStatus()
        );
    }

}
