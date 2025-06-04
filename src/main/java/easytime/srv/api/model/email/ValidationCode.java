package easytime.srv.api.model.email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ValidationCode(@NotNull @NotBlank String code, @NotNull @NotBlank String email, @NotNull @NotBlank String senha) {
}
