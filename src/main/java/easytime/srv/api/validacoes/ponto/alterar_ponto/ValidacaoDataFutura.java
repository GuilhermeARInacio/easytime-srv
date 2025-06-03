package easytime.srv.api.validacoes.ponto.alterar_ponto;

import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.util.DateTimeUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ValidacaoDataFutura implements ValidacaoAlterarPonto{
    public void validar(AlterarPontoDto dto, TimeLog timeLog, String userLogin) {
        var dataPonto = DateTimeUtil.convertUserDateToDBDate(dto.data());
        if (dataPonto.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data não pode ser futura.");
        }
    }
}
