package easytime.srv.api.service;

import easytime.srv.api.infra.exceptions.CampoInvalidoException;
import easytime.srv.api.model.email.EmailRequest;
import easytime.srv.api.tables.PasswordValidationCode;
import easytime.srv.api.tables.repositorys.PasswordValidationCodeRepository;
import easytime.srv.api.tables.repositorys.UserRepository;
import easytime.srv.api.util.PasswordUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
        validateEmail(emailRequest.email()); // Pode lançar IllegalArgumentException

        try {
            var user = userRepository.findByEmail(emailRequest.email())
                    .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            PasswordValidationCode code = createCode();

            String htmlTemplate = carregaTemplate();

            String htmlContent = htmlTemplate
                    .replace("{{login}}", user.getLogin())
                    .replace("{{code}}", code.getCode());

            helper.setFrom(from);
            helper.setTo(emailRequest.email());
            helper.setSubject("Código de validação do reset de senha.");
            helper.setText(htmlContent, true); // Define que o conteúdo é HTML

            javaMailSender.send(message);
        }catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar o e-mail.", e);
        }
    }

    public void validateCode(String code) {
        var validationCode = passwordValidationCodeRepository.findByCode(code.trim())
                .orElseThrow(() -> new IllegalArgumentException("Código não encontrado"));

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
            throw new NotFoundException("Formato de e-mail inválido ou e-mail não encontrado.");
        }
    }

    private String carregaTemplate() {
        try (InputStream inputStream = new ClassPathResource("templates/email-template.html").getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar o template de e-mail.", e);
        }
    }

    private Boolean validateTimestamp(LocalDateTime timestamp) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(timestamp, now);
        return duration.toMinutes() <= 5;
    }
}
