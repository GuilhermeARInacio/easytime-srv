package easytime.srv.api.model.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record DTOUsuario(@Schema(description = "Login do usuário", example = "mkenzo") String login, @Schema(description = "Senha do usuário", example = "Abc@123456") String senha) {
}
