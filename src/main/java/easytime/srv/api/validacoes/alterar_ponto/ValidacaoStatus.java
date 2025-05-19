package easytime.srv.api.validacoes.alterar_ponto;

import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.tables.TimeLog;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoStatus implements ValidacaoAlterarPonto{
    public void validar(AlterarPontoDto dto, TimeLog timeLog) {
        if(timeLog.getStatus() == TimeLog.Status.APROVADO){
            throw new IllegalArgumentException("O ponto já foi aprovado, não tem como ser alterado.");
        }
    }
}
