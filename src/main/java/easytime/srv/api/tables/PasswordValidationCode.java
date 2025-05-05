package easytime.srv.api.tables;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Random;

@Getter
@Entity
@Table(name = "password_validation_code")
public class PasswordValidationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, updatable = false)
    private String code;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    public PasswordValidationCode() {
        this.code = generateCode();
        this.timestamp = LocalDateTime.now();
    }

    public String generateCode() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        String allCharacters = letters + numbers;
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        code.append(letters.charAt(random.nextInt(letters.length())));

        code.append(numbers.charAt(random.nextInt(numbers.length())));

        for (int i = 2; i < 8; i++) {
            code.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        return code.chars()
                .mapToObj(c -> (char) c)
                .sorted((a, b) -> random.nextInt(3) - 1)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
