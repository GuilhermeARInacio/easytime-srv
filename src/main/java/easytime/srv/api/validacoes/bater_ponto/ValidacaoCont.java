package easytime.srv.api.validacoes.bater_ponto;

import easytime.srv.api.tables.TimeLog;

import java.sql.Time;
import java.time.LocalTime;

public class ValidacaoCont implements  ValidacaoPonto{

    @Override
    public void validar(TimeLog timeLog, Time horaAgora) {
        if(timeLog.getCont()>=6){
            throw new IllegalArgumentException("Todas as batidas de ponto já foram registradas para hoje.");
        }
    }
}
