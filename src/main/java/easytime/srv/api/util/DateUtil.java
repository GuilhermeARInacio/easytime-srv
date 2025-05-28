package easytime.srv.api.util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
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
}