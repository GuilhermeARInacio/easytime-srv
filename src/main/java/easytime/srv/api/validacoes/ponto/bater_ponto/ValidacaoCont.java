package easytime.srv.api.validacoes.ponto.bater_ponto;

import easytime.srv.api.tables.TimeLog;
import org.springframework.stereotype.Component;

import java.sql.Time;

@Component
public class ValidacaoCont implements  ValidacaoPonto{

    @Override
    public void validar(TimeLog timeLog, Time horaAgora) {
        if(timeLog.getCont()>=6){
            throw new IllegalArgumentException("Todas as batidas de ponto já foram registradas para hoje.");
        }
    }
}
