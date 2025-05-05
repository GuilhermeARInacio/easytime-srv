package easytime.srv.api.model.email;

// Importing required classes
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Annotations
@Data
@AllArgsConstructor
@NoArgsConstructor

// Class
public class EmailDetails {


    private String from;
    private String to;
    private String subject;
    private String text;
}
