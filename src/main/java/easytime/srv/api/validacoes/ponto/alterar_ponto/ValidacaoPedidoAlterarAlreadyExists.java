package easytime.srv.api.validacoes.ponto.alterar_ponto;

import easytime.srv.api.model.Status;
import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.tables.repositorys.PedidoPontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidacaoPedidoAlterarAlreadyExists implements ValidacaoAlterarPonto{

    @Autowired
    private PedidoPontoRepository pedidoPontoRepository;

    @Override
    public void validar(AlterarPontoDto dto, TimeLog timeLog, String userLogin) throws IllegalArgumentException {
        var pedidoExists = pedidoPontoRepository.existsByPonto_IdAndTipoPedidoAndStatusPedido(dto.idPonto(), PedidoPonto.Tipo.ALTERACAO, Status.APROVADO);
        if(pedidoExists) {
            throw new IllegalArgumentException("Já existe um pedido de alteração para este ponto.");
        }
    }
}
