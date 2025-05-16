package easytime.srv.api.model.pontos;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ConsultaPontosDto(
        @NotBlank
        String login,
        @NotBlank
        LocalDate dtInicio,
        @NotBlank
        LocalDate dtFinal
) {
}
