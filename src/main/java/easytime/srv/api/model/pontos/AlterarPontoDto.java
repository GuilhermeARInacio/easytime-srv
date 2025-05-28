package easytime.srv.api.model.pontos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record AlterarPontoDto(
        @NotNull @NotBlank
        String login,
        @NotNull
        Integer idPonto,
        @NotNull @NotBlank String data,
        LocalTime entrada1,
        LocalTime saida1,
        LocalTime entrada2,
        LocalTime saida2,
        LocalTime entrada3,
        LocalTime saida3
) {
        public boolean isDataValida() {
                if (data == null || data.isBlank()) {
                        return false;
                }
                try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate.parse(data, formatter);
                        return true;
                } catch (DateTimeParseException e) {
                        return false;
                }
        }
}
