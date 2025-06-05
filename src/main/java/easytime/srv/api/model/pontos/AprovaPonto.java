package easytime.srv.api.model.pontos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AprovaPonto(
        @NotBlank @NotNull String userLogin,
        @NotNull Integer idPedido
) {
}
