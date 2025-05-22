package easytime.srv.api.model.pontos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ConsultaPontosDto(
        String login,
        LocalDate dtInicio,
        LocalDate dtFinal
) {
}
