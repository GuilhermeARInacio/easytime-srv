package easytime.srv.api.tables;

import com.fasterxml.jackson.annotation.JsonInclude;
import easytime.srv.api.model.pontos.AlterarPonto;
import easytime.srv.api.model.pontos.AlterarPontoDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pedidos_ponto")
public class PedidoPonto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ponto", nullable = false)
    private TimeLog ponto;

    @Embedded
    private AlterarPonto alteracaoPonto;

    @Setter
    private String gestorLogin;

    @Setter
    private LocalDateTime dataAprovacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipoPedido;

    public enum Tipo{
        ALTERACAO,
        REGISTRO
    }

    private LocalDateTime horarioCriacao;

    public PedidoPonto(AlterarPontoDto dto, TimeLog ponto, User user) {
        this.user = user;
        this.ponto = ponto;
        this.tipoPedido = Tipo.ALTERACAO;
        this.horarioCriacao = LocalDateTime.now();
        this.alteracaoPonto = new AlterarPonto(dto);
    }

    public PedidoPonto(TimeLog ponto) {
        this.user = ponto.getUser();
        this.ponto = ponto;
        this.tipoPedido = Tipo.REGISTRO;
        this.horarioCriacao = LocalDateTime.now();
    }
}
