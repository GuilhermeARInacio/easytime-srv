package easytime.srv.api.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public record DTOUsuario(
        @Schema(description = "Login do usuário", example = "mkenzo")
        @NotNull @NotBlank
        String login,
        @Schema(description = "Senha do usuário", example = "Abc@123456")
        @NotNull @NotBlank
        String senha
) {
}
