
package easytime.srv.api.model.user;

import easytime.srv.api.tables.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

public record UsuarioRetornoDto (
        @Schema(description = "ID do usuário", example = "1")
        Integer id,
        @Schema(description = "Nome do usuário", example = "User")
        String nome,
        @Schema(description = "E-mail do usuário", example = "user@email.com")
        String email,
        @Schema(description = "Login do usuário", example = "user123")
        String login,
        @Schema(description = "Setor do usuário", example = "TI")
        String sector,
        @Schema(description = "Cargo do usuário", example = "Desenvolvedor")
        String jobTitle,
        @Schema(description = "Função do usuário", example = "ROLE_USER")
        String role,
        @Schema(description = "Ativo", example = "true")
        boolean isActive
){

    public UsuarioRetornoDto(User usuario){
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getLogin(),
                usuario.getSector(),
                usuario.getJobTitle(),
                usuario.getRole(),
                usuario.getIsActive());
    }
}