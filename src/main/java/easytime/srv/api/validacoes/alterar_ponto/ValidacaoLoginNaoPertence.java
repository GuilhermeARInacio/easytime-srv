package easytime.srv.api.validacoes.alterar_ponto;

import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.tables.TimeLog;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoLoginNaoPertence implements ValidacaoAlterarPonto{
    public void validar(AlterarPontoDto dto, TimeLog timeLog) {
        if (!timeLog.getUser().getLogin().equals(dto.login())) {
            throw new IllegalArgumentException("O ponto não pertence ao usuário informado.");
        }
    }
}
