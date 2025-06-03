package easytime.srv.api.validacoes.bater_ponto;

import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.validacoes.ponto.bater_ponto.ValidacaoCont;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidacaoContTest {

    private ValidacaoCont validacaoCont;
    private TimeLog timeLog;

    @BeforeEach
    void setUp() {
        validacaoCont = new ValidacaoCont();
        timeLog = new TimeLog();
        timeLog.setData(LocalDate.now());
    }

    @Test
    void validar_ShouldThrowException_WhenContIs6OrMore() {
        // Arrange
        timeLog.setCont(6);
        Time horaAgora = Time.valueOf("08:00:00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validacaoCont.validar(timeLog, horaAgora));
        assertEquals("Todas as batidas de ponto já foram registradas para hoje.", exception.getMessage());
    }

    @Test
    void validar_ShouldNotThrowException_WhenContIsLessThan6() {
        // Arrange
        timeLog.setCont(5);
        Time horaAgora = Time.valueOf("08:00:00");

        // Act & Assert
        validacaoCont.validar(timeLog, horaAgora); // Should not throw any exception
    }
}