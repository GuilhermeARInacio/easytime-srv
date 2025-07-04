package easytime.srv.api.tables.repositorys;

import easytime.srv.api.model.Status;
import easytime.srv.api.model.pontos.PedidoPontoDto;
import easytime.srv.api.tables.PedidoPonto;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoPontoRepository extends JpaRepository<PedidoPonto, Integer> {
    Optional<PedidoPonto> findPedidoPontoByPonto_Id(Integer pontoId);

    Optional<PedidoPonto> findPedidoPontoByPonto_IdAndTipoPedido(Integer pontoId, PedidoPonto.Tipo tipo);

    List<PedidoPonto> findAllByStatusPedido(Status statusPedido);

    @NotNull List<PedidoPonto> findAll();

    boolean existsByPonto_IdAndTipoPedido(Integer pontoId, PedidoPonto.Tipo tipoPedido);

    boolean existsByPonto_IdAndTipoPedidoAndStatusPedidoIn(Integer integer, PedidoPonto.Tipo tipo, List<Status> status);

    boolean existsByPonto_IdAndTipoPedidoAndStatusPedido(Integer integer, PedidoPonto.Tipo tipo, Status status);

    Optional<PedidoPonto> findPedidoPontoByPonto_IdAndTipoPedidoAndStatusPedido(Integer idPonto, PedidoPonto.Tipo tipo, Status status);

    List<PedidoPonto> findAllByPonto_DataBetween(LocalDate dateFinal, LocalDate dateFinal1);

    List<PedidoPonto> findAllByHorarioCriacaoBetween(LocalDateTime dateInicio, LocalDateTime dateFinal);

    List<PedidoPonto> findAllByTipoPedido(PedidoPonto.Tipo tipo);

    List<PedidoPonto> findAllByTipoPedidoAndStatusPedido(PedidoPonto.Tipo tipo, Status status);

    List<PedidoPonto> findAllByStatusPedidoAndHorarioCriacaoBetween(Status status, LocalDateTime dateInicio, LocalDateTime dateFinal);

    List<PedidoPonto> findAllByTipoPedidoAndHorarioCriacaoBetween(PedidoPonto.Tipo tipo, LocalDateTime dateInicio, LocalDateTime dateFinal);

    List<PedidoPonto> findAllByStatusPedidoAndTipoPedidoAndHorarioCriacaoBetween(Status status, PedidoPonto.Tipo tipo, LocalDateTime dateInicio, LocalDateTime dateFinal);
}
