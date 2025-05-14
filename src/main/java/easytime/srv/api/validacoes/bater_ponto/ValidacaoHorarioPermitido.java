package easytime.srv.api.validacoes.bater_ponto;

import easytime.srv.api.tables.TimeLog;

import java.sql.Time;
import java.time.LocalTime;

public class ValidacaoHorarioPermitido implements ValidacaoPonto {

    @Override
    public void validar(TimeLog timeLog, Time horaAgora) {
        LocalTime hora = horaAgora.toLocalTime();

        if (hora.isBefore(LocalTime.of(6, 0)) || hora.isAfter(LocalTime.of(23, 0))) {
            throw new IllegalArgumentException("Horários entre 23h e 6h não são permitidos.");
        }
    }
}