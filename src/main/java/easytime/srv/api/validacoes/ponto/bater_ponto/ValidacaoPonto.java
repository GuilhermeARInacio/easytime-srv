package easytime.srv.api.validacoes.ponto.bater_ponto;

import easytime.srv.api.tables.TimeLog;

import java.sql.Time;
import java.time.LocalTime;

public interface ValidacaoPonto {
    void validar(TimeLog timeLog, Time horaAgora);
}
