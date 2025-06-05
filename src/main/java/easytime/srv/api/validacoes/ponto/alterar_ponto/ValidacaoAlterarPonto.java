package easytime.srv.api.validacoes.ponto.alterar_ponto;

import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.tables.TimeLog;

public interface ValidacaoAlterarPonto {
    void validar(AlterarPontoDto dto, TimeLog timeLog, String userLogin) throws IllegalArgumentException;
}
