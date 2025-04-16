package easytime.srv.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record DTOUsuario(@Schema(description = "Nome do usuário", example = "abc@gmail.com") String usuario, @Schema(description = "Senha do usuário", example = "1234") String senha) {
}
