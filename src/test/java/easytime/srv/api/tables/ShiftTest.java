package easytime.srv.api.tables;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShiftTest {

    @Test
    void shouldCreateShiftObject() {
        // Act
        Shift shift = new Shift();

        // Assert
        assertNotNull(shift);
    }
}