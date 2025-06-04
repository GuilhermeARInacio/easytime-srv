//// src/test/java/easytime/srv/api/model/pontos/AlterarPontoDtoTest.java
//package easytime.srv.api.model.pontos;
//
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class AlterarPontoDtoTest {
//
//    @Test
//    void isDataValida_returnsTrueForValidDate() {
//        AlterarPontoDto dto = new AlterarPontoDto(
//                "user" ,"user", 1, "2024-06-01",
//                LocalTime.of(8,0), null, null, null, null, null
//        );
//        assertTrue(dto.isDataValida());
//    }
//
//    @Test
//    void isDataValida_returnsFalseForNullData() {
//        AlterarPontoDto dto = new AlterarPontoDto(
//                "" ,"user", 1, null,
//                LocalTime.of(8,0), null, null, null, null, null
//        );
//        assertFalse(dto.isDataValida());
//    }
//
//    @Test
//    void isDataValida_returnsFalseForEmptyData() {
//        AlterarPontoDto dto = new AlterarPontoDto(
//                "","user", 1, "",
//                LocalTime.of(8,0), null, null, null, null, null
//        );
//        assertFalse(dto.isDataValida());
//    }
//
//    @Test
//    void isDataValida_throwsExceptionForInvalidDateFormat() {
//        AlterarPontoDto dto = new AlterarPontoDto(
//                "","user", 1, "invalid-date",
//                LocalTime.of(8,0), null, null, null, null, null
//        );
//        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, dto::isDataValida);
//        assertTrue(ex.getMessage().contains("Data inválida"));
//    }
//}