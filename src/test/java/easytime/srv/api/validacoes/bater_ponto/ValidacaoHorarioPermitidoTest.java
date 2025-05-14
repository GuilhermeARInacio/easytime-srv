package easytime.srv.api.validacoes.bater_ponto;

import easytime.srv.api.tables.TimeLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidacaoHorarioPermitidoTest {

    private ValidacaoHorarioPermitido validacaoHorarioPermitido;
    private TimeLog timeLog;

    @BeforeEach
    void setUp() {
        validacaoHorarioPermitido = new ValidacaoHorarioPermitido();
        timeLog = new TimeLog(); // Create a dummy TimeLog object
    }

    @Test
    void validar_ShouldThrowException_WhenHoraIsBefore6AM() {
        // Arrange
        Time horaAgora = Time.valueOf("05:59:00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validacaoHorarioPermitido.validar(timeLog, horaAgora));
        assertEquals("Horários entre 23h e 6h não são permitidos.", exception.getMessage());
    }

    @Test
    void validar_ShouldThrowException_WhenHoraIsAfter11PM() {
        // Arrange
        Time horaAgora = Time.valueOf("23:01:00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validacaoHorarioPermitido.validar(timeLog, horaAgora));
        assertEquals("Horários entre 23h e 6h não são permitidos.", exception.getMessage());
    }

    @Test
    void validar_ShouldNotThrowException_WhenHoraIsWithinAllowedRange() {
        // Arrange
        Time horaAgora = Time.valueOf("12:00:00");

        // Act & Assert
        validacaoHorarioPermitido.validar(timeLog, horaAgora); // Should not throw any exception
    }
}