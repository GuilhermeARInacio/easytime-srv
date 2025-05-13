package easytime.srv.api.model.pontos;

import easytime.srv.api.tables.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TimeLogDto(
        User user,
        LocalDate data,
        LocalDateTime E1,
        LocalDateTime S1,
        LocalDateTime E2,
        LocalDateTime S2,
        LocalDateTime E3,
        LocalDateTime S3
) {
}
