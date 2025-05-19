package easytime.srv.api.model.pontos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;

public record AlterarPontoDto(
        @NotNull(message = "O login não pode ser nulo") @NotBlank(message = "O login não pode ser nulo")
        String login,
        @NotNull(message = "O id não pode ser nulo")
        Integer idPonto,
        String data,
        LocalTime entrada1,
        LocalTime saida1,
        LocalTime entrada2,
        LocalTime saida2,
        LocalTime entrada3,
        LocalTime saida3
) {
        public boolean isDataValida() {
                if (data == null && data.trim().isEmpty()){
                        return false;
                }
                try {
                        LocalDate.parse(data);
                        return true;
                } catch (DateTimeException e) {
                        throw new IllegalArgumentException("Data inválida: " + data, e);
                }
        }
}
