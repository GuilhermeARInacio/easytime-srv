package easytime.srv.api.model.user;

import jakarta.validation.constraints.NotBlank;

public record LoginDto (@NotBlank String login){
}
