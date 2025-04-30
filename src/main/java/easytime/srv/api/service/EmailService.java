package easytime.srv.api.service;

import easytime.srv.api.tables.PasswordValidationCode;
import easytime.srv.api.tables.repositorys.PasswordValidationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    private PasswordValidationCodeRepository passwordValidationCodeRepository;

    public PasswordValidationCode createCode() {
        return passwordValidationCodeRepository.save(new PasswordValidationCode());
    }

    public boolean validateCode(String code) {
        Optional<PasswordValidationCode> validationCode = passwordValidationCodeRepository.findByCode(code);
        if (validationCode.isPresent()) {
            PasswordValidationCode codeObj = validationCode.get();
            if (validateTimestamp(codeObj.getTimestamp())) {
                passwordValidationCodeRepository.delete(codeObj);
                return true;
            } else {
                passwordValidationCodeRepository.delete(codeObj);
                return false;
            }
        } else {
            throw new IllegalArgumentException("Código inválido ou expirado.");
        }
    }

    public void validateEmail(String email) {
        try {
            new jakarta.mail.internet.InternetAddress(email).validate();
        } catch (jakarta.mail.internet.AddressException e) {
            throw new IllegalArgumentException("Formato de e-mail inválido.");
        }
    }

    private boolean validateTimestamp(LocalDateTime timestamp) {
        LocalDateTime now = LocalDateTime.now();

        System.out.println(now);
        Duration duration = Duration.between(timestamp, now);
        System.out.println(duration.toMinutes());
        return duration.toMinutes() <= 5;
    }
}
