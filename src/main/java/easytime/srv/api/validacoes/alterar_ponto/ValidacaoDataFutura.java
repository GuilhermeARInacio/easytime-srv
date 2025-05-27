package easytime.srv.api.validacoes.alterar_ponto;

import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.tables.TimeLog;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ValidacaoDataFutura implements ValidacaoAlterarPonto{
    public void validar(AlterarPontoDto dto, TimeLog timeLog) {
        if (LocalDate.parse(dto.data()).isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data não pode ser futura.");
        }
    }
}
