package easytime.srv.api.model.pontos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ConsultaPontosDto(
        @NotBlank @NotNull
        String login,
        @NotBlank @NotNull
        LocalDate dtInicio,
        @NotBlank @NotNull
        LocalDate dtFinal
) {
}
