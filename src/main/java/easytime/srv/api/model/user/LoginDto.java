package easytime.srv.api.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginDto (@Schema(description = "Login usado para bater ponto", example = "mkenzo") @NotNull @NotBlank String userLogin){
}
