package easytime.srv.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record DTOUsuario(@Schema(description = "Login do usuário", example = "gainacio") String login, @Schema(description = "Senha do usuário", example = "1234") String senha) {
}
