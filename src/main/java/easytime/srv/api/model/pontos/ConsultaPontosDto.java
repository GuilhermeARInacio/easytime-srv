package easytime.srv.api.model.pontos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ConsultaPontosDto(
        @NotBlank(message = "O login deve ser preenchido.") @NotNull(message = "O login deve ser preenchido.")
        String login,
        @NotNull(message = "A data de inicio deve ser preenchida.")
        LocalDate dtInicio,
        @NotNull(message = "A data final deve ser preenchida.")
        LocalDate dtFinal
) {
}
