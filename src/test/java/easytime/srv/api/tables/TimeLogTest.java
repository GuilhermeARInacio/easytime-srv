package easytime.srv.api.tables;

import easytime.srv.api.model.pontos.AlterarPontoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TimeLogTest {

    private TimeLog timeLog;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setLogin("testUser");
        timeLog = new TimeLog(user, LocalDate.now());
    }

    @Test
    void testSetPonto_Success() {
        // Arrange
        Time hora = Time.valueOf("08:00:00");

        // Act
        timeLog.setPonto(hora);

        // Assert
        assertEquals(hora, timeLog.getE1());
        assertEquals(1, timeLog.getCont());
    }

    @Test
    void testSetPonto_MultipleEntries() {
        // Arrange
        Time hora1 = Time.valueOf("08:00:00");
        Time hora2 = Time.valueOf("12:00:00");

        // Act
        timeLog.setPonto(hora1);
        timeLog.setPonto(hora2);

        // Assert
        assertEquals(hora1, timeLog.getE1());
        assertEquals(hora2, timeLog.getS1());
        assertEquals(2, timeLog.getCont());
    }

    @Test
    void testGetUltimoBatimentoName_Entrada() {
        // Act
        String result = timeLog.getUltimoBatimentoName(0);

        // Assert
        assertEquals("E1", result);
    }

    @Test
    void testGetUltimoBatimentoName_Saida() {
        // Act
        String result = timeLog.getUltimoBatimentoName(1);

        // Assert
        assertEquals("S1", result);
    }

    @Test
    void testGetUltimoBatimentoValue_Success() {
        // Arrange
        Time hora = Time.valueOf("08:00:00");
        timeLog.setPonto(hora);

        // Act
        Object result = timeLog.getUltimoBatimentoValue();

        // Assert
        assertEquals(hora, result);
    }

    @Test
    void testGetUltimoBatimentoValue_FieldInexistente() {
        // Arrange
        timeLog.setCont(7);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> timeLog.getUltimoBatimentoValue());
        assertTrue(exception.getMessage().contains("Erro acessando atributo:"));
    }

    @Test
    void testSetPonto_InvalidField() {
        // Arrange
        timeLog.setCont(7); // Invalid cont value
        Time hora = Time.valueOf("08:00:00");

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> timeLog.setPonto(hora));
        assertTrue(exception.getMessage().contains("Erro ao definir o campo"));
    }

    @Test
    void alterarPonto_updatesAllFields() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        when(dto.data()).thenReturn("2024-06-02");
        when(dto.entrada1()).thenReturn(LocalTime.parse("08:00:00"));
        when(dto.saida1()).thenReturn(LocalTime.parse("12:00:00"));
        when(dto.entrada2()).thenReturn(LocalTime.parse("13:00:00"));
        when(dto.saida2()).thenReturn(LocalTime.parse("17:00:00"));
        when(dto.entrada3()).thenReturn(LocalTime.parse("18:00:00"));
        when(dto.saida3()).thenReturn(LocalTime.parse("19:00:00"));

        timeLog.alterarPonto(dto);

        assertEquals(LocalDate.of(2024, 6, 2), timeLog.getData());
        assertEquals(Time.valueOf("08:00:00"), timeLog.getE1());
        assertEquals(Time.valueOf("12:00:00"), timeLog.getS1());
        assertEquals(Time.valueOf("13:00:00"), timeLog.getE2());
        assertEquals(Time.valueOf("17:00:00"), timeLog.getS2());
        assertEquals(Time.valueOf("18:00:00"), timeLog.getE3());
        assertEquals(Time.valueOf("19:00:00"), timeLog.getS3());
    }

    @Test
    void alterarPonto_nullFields_doNotUpdate() {
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        when(dto.data()).thenReturn(null);
        when(dto.entrada1()).thenReturn(null);
        when(dto.saida1()).thenReturn(null);
        when(dto.entrada2()).thenReturn(null);
        when(dto.saida2()).thenReturn(null);
        when(dto.entrada3()).thenReturn(null);
        when(dto.saida3()).thenReturn(null);

        LocalDate originalDate = timeLog.getData();
        timeLog.setE1(Time.valueOf("07:00:00"));

        timeLog.alterarPonto(dto);

        assertEquals(originalDate, timeLog.getData());
        assertEquals(Time.valueOf("07:00:00"), timeLog.getE1());
    }
}