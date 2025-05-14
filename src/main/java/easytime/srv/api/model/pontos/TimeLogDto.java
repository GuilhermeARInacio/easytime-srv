package easytime.srv.api.model.pontos;

import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.tables.User;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimeLogDto(
        String login,
        LocalDate data,
        LocalTime horarioBatida,
        TimeLog.Status status
) {

    public TimeLogDto(TimeLog timeLog){
        this(
                timeLog.getUser().getLogin(),
                timeLog.getData(),
                switch (timeLog.getCont()){
                    case 6 -> timeLog.getS3().toLocalTime();
                    case 5 -> timeLog.getE3().toLocalTime();
                    case 4 -> timeLog.getS2().toLocalTime();
                    case 3 -> timeLog.getE2().toLocalTime();
                    case 2 -> timeLog.getS1().toLocalTime();
                    case 1 -> timeLog.getE1().toLocalTime();
                    default -> null;
                },
                timeLog.getStatus()
        );
    }
}
