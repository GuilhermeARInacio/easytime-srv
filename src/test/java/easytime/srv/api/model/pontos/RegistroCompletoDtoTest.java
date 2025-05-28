// src/test/java/easytime/srv/api/model/pontos/RegistroCompletoDtoTest.java
package easytime.srv.api.model.pontos;

import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.tables.User;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistroCompletoDtoTest {

    @Test
    void constructor_setsAllFields() {
        RegistroCompletoDto dto = new RegistroCompletoDto(
                1, "user", "",
                Time.valueOf("8:00:00"),
                Time.valueOf("08:00:00"), Time.valueOf("12:00:00"),
                Time.valueOf("13:00:00"), Time.valueOf("17:00:00"),
                null, null,
                TimeLog.Status.PENDENTE
        );

        assertEquals(1, dto.id());
        assertEquals("user", dto.login());
        assertEquals("", dto.data());
        assertEquals(Time.valueOf("8:00:00"), dto.horasTrabalhadas());
        assertEquals(Time.valueOf("08:00:00"), dto.entrada1());
        assertEquals(Time.valueOf("12:00:00"), dto.saida1());
        assertEquals(Time.valueOf("13:00:00"), dto.entrada2());
        assertEquals(Time.valueOf("17:00:00"), dto.saida2());
        assertNull(dto.entrada3());
        assertNull(dto.saida3());
        assertEquals(TimeLog.Status.PENDENTE, dto.status());
    }

    @Test
    void constructor_fromTimeLog_setsAllFields() {
        User user = mock(User.class);
        when(user.getLogin()).thenReturn("user1");
        TimeLog timeLog = mock(TimeLog.class);
        when(timeLog.getId()).thenReturn(10);
        when(timeLog.getUser()).thenReturn(user);
        when(timeLog.getData()).thenReturn(LocalDate.of(2024, 6, 2));
        when(timeLog.getHoras_trabalhadas()).thenReturn(Time.valueOf("7:30:00"));
        when(timeLog.getE1()).thenReturn(Time.valueOf("08:00:00"));
        when(timeLog.getS1()).thenReturn(Time.valueOf("12:00:00"));
        when(timeLog.getE2()).thenReturn(Time.valueOf("13:00:00"));
        when(timeLog.getS2()).thenReturn(Time.valueOf("16:30:00"));
        when(timeLog.getE3()).thenReturn(null);
        when(timeLog.getS3()).thenReturn(null);
        when(timeLog.getStatus()).thenReturn(TimeLog.Status.APROVADO);

        RegistroCompletoDto dto = new RegistroCompletoDto(timeLog);

        assertEquals(10, dto.id());
        assertEquals("user1", dto.login());
        assertEquals(LocalDate.of(2024, 6, 2), dto.data());
        assertEquals(Time.valueOf("7:30:00"), dto.horasTrabalhadas());
        assertEquals(Time.valueOf("08:00:00"), dto.entrada1());
        assertEquals(Time.valueOf("12:00:00"), dto.saida1());
        assertEquals(Time.valueOf("13:00:00"), dto.entrada2());
        assertEquals(Time.valueOf("16:30:00"), dto.saida2());
        assertNull(dto.entrada3());
        assertNull(dto.saida3());
        assertEquals(TimeLog.Status.APROVADO, dto.status());
    }
}