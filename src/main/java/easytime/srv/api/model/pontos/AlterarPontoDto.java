package easytime.srv.api.model.pontos;

import java.time.LocalTime;

public record AlterarPontoDto(
        String login,
        Integer idPonto,
        String data,
        LocalTime entrada1,
        LocalTime saida1,
        LocalTime entrada2,
        LocalTime saida2,
        LocalTime entrada3,
        LocalTime saida3,
        String justificativa
) {
}
