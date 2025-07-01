// File: src/test/java/easytime/srv/api/util/DateTimeUtilTest.java
package easytime.srv.api.util;

import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeUtilTest {

    @Test
    void convertUserDateToDBDate_ValidDate_ReturnsLocalDate() {
        LocalDate expected = LocalDate.of(2024, 6, 1);
        assertEquals(expected, DateTimeUtil.convertUserDateToDBDate("01/06/2024"));
    }

    @Test
    void convertUserDateToDBDate_InvalidFormat_ThrowsException() {
        assertThrows(DateTimeException.class, () -> DateTimeUtil.convertUserDateToDBDate("2024-06-01"));
        assertThrows(DateTimeException.class, () -> DateTimeUtil.convertUserDateToDBDate("32/01/2024"));
        assertThrows(DateTimeException.class, () -> DateTimeUtil.convertUserDateToDBDate("abc"));
    }

    @Test
    void convertDBDateToUserDate_ValidDate_ReturnsString() {
        LocalDate date = LocalDate.of(2024, 6, 1);
        assertEquals("01/06/2024", DateTimeUtil.convertDBDateToUserDate(date));
    }

    @Test
    void convertDBDateTimeToUserDateTime_ValidDateTime_ReturnsString() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 6, 1, 14, 30);
        assertEquals("01/06/2024 - 14:30", DateTimeUtil.convertDBDateTimeToUserDateTime(dateTime));
    }

    @Test
    void convertDBDateTimeToUserDateTime_Null_ReturnsNull() {
        assertNull(DateTimeUtil.convertDBDateTimeToUserDateTime(null));
    }
}