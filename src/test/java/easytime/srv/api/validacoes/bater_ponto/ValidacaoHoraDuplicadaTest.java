package easytime.srv.api.validacoes.bater_ponto;

import easytime.srv.api.tables.TimeLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidacaoHoraDuplicadaTest {

    private ValidacaoHoraDuplicada validacaoHoraDuplicada;
    private TimeLog timeLog;

    @BeforeEach
    void setUp() {
        validacaoHoraDuplicada = new ValidacaoHoraDuplicada();
        timeLog = mock(TimeLog.class); // Mock the TimeLog object
        when(timeLog.getData()).thenReturn(LocalDate.now());
    }

    @Test
    void validar_ShouldThrowException_WhenIntervalIsLessThan2Minutes() {
        // Arrange
        when(timeLog.getUltimoBatimentoValue()).thenReturn(Time.valueOf("08:00:00"));
        Time horaAgora = Time.valueOf("08:01:00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validacaoHoraDuplicada.validar(timeLog, horaAgora));
        assertEquals("O intervalo entre os batimentos deve ser de pelo menos 2 minutos.", exception.getMessage());
    }

    @Test
    void validar_ShouldNotThrowException_WhenIntervalIs2MinutesOrMore() {
        // Arrange
        when(timeLog.getUltimoBatimentoValue()).thenReturn(Time.valueOf("08:00:00"));
        Time horaAgora = Time.valueOf("08:02:00");

        // Act & Assert
        validacaoHoraDuplicada.validar(timeLog, horaAgora); // Should not throw any exception
    }

    @Test
    void validar_ShouldNotThrowException_WhenNoPreviousBatimentoExists() {
        // Arrange
        when(timeLog.getUltimoBatimentoValue()).thenReturn(null); // Mock no previous batimento
        Time horaAgora = Time.valueOf("08:00:00");

        // Act & Assert
        validacaoHoraDuplicada.validar(timeLog, horaAgora); // Should not throw any exception
    }
}