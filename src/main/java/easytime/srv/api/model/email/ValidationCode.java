package easytime.srv.api.model.email;

import jakarta.validation.constraints.NotBlank;

public record ValidationCode(@NotBlank String code, @NotBlank String email, @NotBlank String senha) {
}
