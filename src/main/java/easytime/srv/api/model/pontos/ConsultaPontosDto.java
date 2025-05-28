package easytime.srv.api.model.pontos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ConsultaPontosDto(
        @NotNull @NotBlank String login,
        @NotNull @NotBlank String dtInicio,
        @NotNull @NotBlank String dtFinal
) {
}
