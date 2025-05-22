package easytime.srv.api.model.pontos;

import com.fasterxml.jackson.annotation.JsonProperty;
import easytime.srv.api.tables.TimeLog;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;

public record RegistroCompletoDto (
        Integer id,
        String login,
        LocalDate data,
        Duration horasTrabalhadas,
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
                timeLog.getData(),
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

    @JsonProperty("horasTrabalhadas")
    public String horasTrabalhadasFormatado() {
        if (horasTrabalhadas == null) return "00:00";
        long hours = horasTrabalhadas.toHours();
        long minutes = horasTrabalhadas.toMinutes() % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}