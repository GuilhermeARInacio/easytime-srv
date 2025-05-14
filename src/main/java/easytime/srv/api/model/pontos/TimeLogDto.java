package easytime.srv.api.model.pontos;

import easytime.srv.api.tables.User;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimeLogDto(
        User user,
        LocalDate data,
        LocalTime E1,
        LocalTime S1,
        LocalTime E2,
        LocalTime S2,
        LocalTime E3,
        LocalTime S3
) {
}
