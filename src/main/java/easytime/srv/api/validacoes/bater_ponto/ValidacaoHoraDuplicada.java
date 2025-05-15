package easytime.srv.api.validacoes.bater_ponto;

import easytime.srv.api.tables.TimeLog;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.Duration;

@Component
public class ValidacaoHoraDuplicada implements ValidacaoPonto{

    @Override
    public void validar(TimeLog timeLog, Time horaAgora) {
        var ultimoBatimento = timeLog.getUltimoBatimentoValue();

        if (ultimoBatimento == null) {
            return;
        }

        if (ultimoBatimento instanceof Time ultimoHorario) {
            Duration diferenca = Duration.between(ultimoHorario.toLocalTime(), horaAgora.toLocalTime());

            if (diferenca.toMinutes() < 2) {
                throw new IllegalArgumentException("O intervalo entre os batimentos deve ser de pelo menos 2 minutos.");
            }
        }
    }
}
