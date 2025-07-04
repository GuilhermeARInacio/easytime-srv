package easytime.srv.api.model.pontos;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Embeddable
public class AlterarPonto {
    private LocalTime entrada1;
    private LocalTime saida1;
    private LocalTime entrada2;
    private LocalTime saida2;
    private LocalTime entrada3;
    private LocalTime saida3;
    private String justificativa;

    public AlterarPonto(AlterarPontoDto dto) {
        this.entrada1 = dto.entrada1();
        this.saida1 = dto.saida1();
        this.entrada2 = dto.entrada2();
        this.saida2 = dto.saida2();
        this.entrada3 = dto.entrada3();
        this.saida3 = dto.saida3();
        this.justificativa = dto.justificativa();
    }
}
