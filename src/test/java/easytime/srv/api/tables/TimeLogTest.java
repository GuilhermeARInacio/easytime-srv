// File: src/test/java/easytime/srv/api/tables/TimeLogTest.java
package easytime.srv.api.tables;

import easytime.srv.api.model.pontos.AlterarPonto;
import easytime.srv.api.util.DateTimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TimeLogTest {

    private TimeLog timeLog;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setLogin("testUser");
        timeLog = new TimeLog(user, LocalDate.of(2024, 6, 1));
    }

    @Test
    void setPonto_setsE1_andIncrementsCont() {
        Time hora = Time.valueOf("08:00:00");
        timeLog.setPonto(hora);
        assertEquals(hora, timeLog.getE1());
        assertEquals(1, timeLog.getCont());
    }

    @Test
    void setPonto_setsS1_andCalculatesHorasTrabalhadas() {
        timeLog.setPonto(Time.valueOf("08:00:00")); // E1
        timeLog.setPonto(Time.valueOf("12:00:00")); // S1
        assertEquals(Time.valueOf("12:00:00"), timeLog.getS1());
        assertTrue(timeLog.getHoras_trabalhadas().getTime() > 0);
        assertEquals(2, timeLog.getCont());
    }

    @Test
    void getUltimoBatimentoName_returnsCorrectField() {
        assertEquals("E1", timeLog.getUltimoBatimentoName(0));
        assertEquals("S1", timeLog.getUltimoBatimentoName(1));
        assertEquals("E2", timeLog.getUltimoBatimentoName(2));
        assertEquals("S2", timeLog.getUltimoBatimentoName(3));
    }

    @Test
    void getUltimoBatimentoValue_returnsNullIfNoEntries() {
        assertNull(timeLog.getUltimoBatimentoValue());
    }

    @Test
    void getUltimoBatimentoValue_returnsLastValue() {
        Time hora = Time.valueOf("08:00:00");
        timeLog.setPonto(hora);
        assertEquals(hora, timeLog.getUltimoBatimentoValue());
    }

    @Test
    void setPonto_invalidCont_throwsException() {
        timeLog.setCont(7); // Invalid field
        Time hora = Time.valueOf("08:00:00");
        RuntimeException ex = assertThrows(RuntimeException.class, () -> timeLog.setPonto(hora));
        assertTrue(ex.getMessage().contains("Erro ao definir o campo"));
    }

    @Test
    void getUltimoBatimentoValue_invalidCont_throwsException() {
        timeLog.setCont(7);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> timeLog.getUltimoBatimentoValue());
        assertTrue(ex.getMessage().contains("Erro acessando atributo:"));
    }

    @Test
    void alterarPonto_updatesFieldsAndCalculatesHoras() {
        AlterarPonto dto = mock(AlterarPonto.class);
        when(dto.getData()).thenReturn("02/06/2024");
        when(dto.getEntrada1()).thenReturn(LocalTime.of(8, 0));
        when(dto.getSaida1()).thenReturn(LocalTime.of(12, 0));
        when(dto.getEntrada2()).thenReturn(LocalTime.of(13, 0));
        when(dto.getSaida2()).thenReturn(LocalTime.of(17, 0));
        when(dto.getEntrada3()).thenReturn(LocalTime.of(18, 0));
        when(dto.getSaida3()).thenReturn(LocalTime.of(19, 0));

        timeLog.alterarPonto(dto);

        assertEquals(DateTimeUtil.convertUserDateToDBDate("02/06/2024"), timeLog.getData());
        assertEquals(Time.valueOf("08:00:00"), timeLog.getE1());
        assertEquals(Time.valueOf("12:00:00"), timeLog.getS1());
        assertEquals(Time.valueOf("13:00:00"), timeLog.getE2());
        assertEquals(Time.valueOf("17:00:00"), timeLog.getS2());
        assertEquals(Time.valueOf("18:00:00"), timeLog.getE3());
        assertEquals(Time.valueOf("19:00:00"), timeLog.getS3());
        assertTrue(timeLog.getHoras_trabalhadas().getTime() > 0);
    }

    @Test
    void alterarPonto_nullFields_doNotUpdate() {
        AlterarPonto dto = mock(AlterarPonto.class);
        when(dto.getData()).thenReturn(null);
        when(dto.getEntrada1()).thenReturn(null);
        when(dto.getSaida1()).thenReturn(null);
        when(dto.getEntrada2()).thenReturn(null);
        when(dto.getSaida2()).thenReturn(null);
        when(dto.getEntrada3()).thenReturn(null);
        when(dto.getSaida3()).thenReturn(null);

        LocalDate originalDate = timeLog.getData();
        timeLog.setE1(Time.valueOf("07:00:00"));

        timeLog.alterarPonto(dto);

        assertEquals(originalDate, timeLog.getData());
        assertEquals(Time.valueOf("07:00:00"), timeLog.getE1());
    }
}