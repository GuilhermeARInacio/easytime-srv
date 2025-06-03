package easytime.srv.api.util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static LocalDate convertUserDateToDBDate(String dateBr) {
        try{
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dateBr, inputFormatter);
        }catch(Exception e){
            throw new DateTimeException("Data inválida: " + dateBr + ", Por favor, insira uma data válida no formato DD/MM/AAAA.");
        }
    }

    public static String convertDBDateToUserDate(LocalDate dateBr) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dateBr.format(outputFormatter);
    }

    public static String convertDBDateTimeToUserDateTime(LocalDateTime dateBr) {
        if(dateBr == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");
        return dateBr.format(formatter);
    }
}