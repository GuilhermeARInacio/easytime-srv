package easytime.srv.api.service;

import easytime.srv.api.infra.exceptions.CampoInvalidoException;
import easytime.srv.api.model.email.EmailRequest;
import easytime.srv.api.tables.PasswordValidationCode;
import easytime.srv.api.tables.repositorys.PasswordValidationCodeRepository;
import easytime.srv.api.tables.repositorys.UserRepository;
import easytime.srv.api.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    private PasswordValidationCodeRepository passwordValidationCodeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public PasswordValidationCode createCode() {
        return passwordValidationCodeRepository.save(new PasswordValidationCode());
    }

    public void sendEmail(EmailRequest emailRequest) {
        validateEmail(emailRequest.email()); //pode throw IllegalArgumentException

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(emailRequest.email());
        mailMessage.setSubject("Código de validação do reset de senha.");
        PasswordValidationCode code = createCode();
        mailMessage.setText("Envie o seguinte código: "+code.getCode());
        javaMailSender.send(mailMessage);
    }

    public void validateCode(String code) {
        var validationCode = passwordValidationCodeRepository.findByCode(code.trim())
                .orElseThrow(() -> new NotFoundException("Código não encontrado"));

        if(validateTimestamp(validationCode.getTimestamp())){
            passwordValidationCodeRepository.delete(validationCode);
        } else {
            passwordValidationCodeRepository.delete(validationCode);
            throw new IllegalArgumentException("Código inválido ou expirado.");
        }
    }

    public void redefinirSenha(String email, String senha){
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        if(PasswordUtil.isPasswordValid(senha)){
            user.setPassword(PasswordUtil.encryptPassword(senha));
            userRepository.save(user);
        } else{
            throw new CampoInvalidoException("Formato de senha inválido.");
        }
    }

    public void validateEmail(String email) {
        try {
            new jakarta.mail.internet.InternetAddress(email).validate();
        } catch (jakarta.mail.internet.AddressException e) {
            throw new IllegalArgumentException("Formato de e-mail inválido ou e-mail não encontrado.");
        }
    }

    private Boolean validateTimestamp(LocalDateTime timestamp) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(timestamp, now);
        return duration.toMinutes() <= 5;
    }
}
