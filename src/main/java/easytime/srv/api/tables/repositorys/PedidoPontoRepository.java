package easytime.srv.api.tables.repositorys;

import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.tables.TimeLog;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoPontoRepository extends JpaRepository<PedidoPonto, Integer> {
    Optional<PedidoPonto> findPedidoPontoByPonto_Id(Integer pontoId);
    List<PedidoPonto> findAllByStatus(PedidoPonto.Status status);
    @NotNull List<PedidoPonto> findAll();

    boolean existsByPonto_IdAndTipoPedido(Integer pontoId, PedidoPonto.Tipo tipoPedido);
}
