package easytime.srv.api.controller;

import easytime.srv.api.model.email.EmailRequest;
import easytime.srv.api.service.EmailService;
import easytime.srv.api.tables.PasswordValidationCode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.username}")
    private String from;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody @Valid EmailRequest emailRequest) {
        try{
            emailService.validateEmail(emailRequest.email());

            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(from);
            mailMessage.setTo(emailRequest.email());
            mailMessage.setSubject("Código de validação do reset de senha.");
            PasswordValidationCode code = emailService.createCode();
            mailMessage.setText("Envie o seguinte código: "+code.getCode());
            System.out.println(code.toString());
            javaMailSender.send(mailMessage);

            return ResponseEntity.ok("Email enviado, verifique sua caixa de entrada ou spam.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }

    @PostMapping("/validate-code")
    public ResponseEntity<String> validateCode(@RequestBody @Valid String code) {
        try {
            if (emailService.validateCode(code)) {
                return ResponseEntity.ok("Código válido.");
            } else {
                return ResponseEntity.badRequest().body("Código inválido ou expirado.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }
}
