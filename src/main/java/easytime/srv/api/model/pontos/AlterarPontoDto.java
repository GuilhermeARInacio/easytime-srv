package easytime.srv.api.model.pontos;

import easytime.srv.api.tables.TimeLog;

import java.time.LocalTime;

public record AlterarPontoDto(
        Integer idPonto,
        String data,
        LocalTime entrada1,
        LocalTime saida1,
        LocalTime entrada2,
        LocalTime saida2,
        LocalTime entrada3,
        LocalTime saida3
) {
}
