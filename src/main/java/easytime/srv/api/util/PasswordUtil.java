package easytime.srv.api.util;

import easytime.srv.api.infra.exceptions.CampoInvalidoException;
import easytime.srv.api.infra.exceptions.CampoVazioException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encryptPassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public static boolean validatePassword(String rawPassword, String encryptedPassword) {
        return encoder.matches(rawPassword, encryptedPassword);
    }

    public static boolean isPasswordValid(String password) {
        Boolean senhaVazia = password.isBlank();
        Boolean tamanhoInvalido = password.length() < 8;
        Boolean temLetras = password.matches(".*[a-zA-Z].*");
        Boolean temNumeros = password.matches(".*\\d.*");
        Boolean temCaracteresEspeciais = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");

        return !senhaVazia && !tamanhoInvalido && temLetras && temNumeros && temCaracteresEspeciais;
    }
}