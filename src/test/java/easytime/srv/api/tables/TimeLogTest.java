package easytime.srv.api.tables;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.sql.Time;
import java.time.LocalDate;

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
    void testGetUltimoBatimentoValue_NoBatimento() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, timeLog::getUltimoBatimentoValue);
        assertTrue(exception.getMessage().contains("No batimento exists to retrieve."));
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
}