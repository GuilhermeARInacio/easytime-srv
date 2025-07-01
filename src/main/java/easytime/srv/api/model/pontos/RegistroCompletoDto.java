package easytime.srv.api.model.pontos;

import easytime.srv.api.model.Status;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.util.DateTimeUtil;

import java.sql.Time;

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
        Status status,
        boolean temAlteracao
){

    public RegistroCompletoDto(TimeLog timeLog, boolean temAlteracao) {
        this(
                timeLog.getId(),
                timeLog.getUser().getLogin(),
                DateTimeUtil.convertDBDateToUserDate(timeLog.getData()),
                timeLog.getHoras_trabalhadas(),
                timeLog.getE1(),
                timeLog.getS1(),
                timeLog.getE2(),
                timeLog.getS2(),
                timeLog.getE3(),
                timeLog.getS3(),
                timeLog.getStatusRegistro(),
                temAlteracao
        );
    }


}
