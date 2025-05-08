package easytime.srv.api.service;

import easytime.srv.api.infra.exceptions.CampoInvalidoException;
import easytime.srv.api.model.email.EmailRequest;
import easytime.srv.api.tables.PasswordValidationCode;
import easytime.srv.api.tables.User;
import easytime.srv.api.tables.repositorys.PasswordValidationCodeRepository;
import easytime.srv.api.tables.repositorys.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.webjars.NotFoundException;

import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private PasswordValidationCodeRepository passwordValidationCodeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verifica se o código de validação é enviado por e-mail")
    void shouldSendEmailSuccessfully() {
        EmailRequest emailRequest = new EmailRequest("test@example.com");
        PasswordValidationCode code = new PasswordValidationCode();
        User user = new User();
        user.setLogin("testUser");

        ReflectionTestUtils.setField(emailService, "from", "noreply@example.com");

        when(passwordValidationCodeRepository.save(any())).thenReturn(code);
        when(userRepository.findByEmail(emailRequest.email())).thenReturn(Optional.of(user));
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendEmail(emailRequest);

        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    @DisplayName("Verifica se o e-mail inválido lança exceção")
    void shouldThrowExceptionForInvalidEmail() {
        EmailRequest emailRequest = new EmailRequest("invalid-email");

        assertThrows(NotFoundException.class, () -> emailService.sendEmail(emailRequest));
        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Verifica se o e-mail não encontrado lança exceção")
    void shouldThrowExceptionForNonExistentUserInSendEmail() {
        EmailRequest emailRequest = new EmailRequest("test@example.com");

        when(userRepository.findByEmail(emailRequest.email())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> emailService.sendEmail(emailRequest));
        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Verifica se o código de validação é validado com sucesso")
    void shouldValidateCodeSuccessfully() {
        PasswordValidationCode code = new PasswordValidationCode();
        when(passwordValidationCodeRepository.findByCode(anyString())).thenReturn(Optional.of(code));

        assertDoesNotThrow(() -> emailService.validateCode("valid-code"));
        verify(passwordValidationCodeRepository, times(1)).delete(code);
    }

    @Test
    @DisplayName("Verifica se o código de validação não é encontrado e lança exceção")
    void shouldThrowExceptionForInvalidCode() {
        when(passwordValidationCodeRepository.findByCode(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> emailService.validateCode("invalid-code"));
    }

    @Test
    @DisplayName("Verifica se o código de validação expirado lança exceção")
    void shouldThrowExceptionForExpiredCode() throws NoSuchFieldException, IllegalAccessException {
        PasswordValidationCode code = new PasswordValidationCode();
        var field = PasswordValidationCode.class.getDeclaredField("timestamp");
        field.setAccessible(true);
        field.set(code, LocalDateTime.now().minusMinutes(10));

        when(passwordValidationCodeRepository.findByCode(anyString())).thenReturn(Optional.of(code));

        assertThrows(IllegalArgumentException.class, () -> emailService.validateCode("expired-code"));
        verify(passwordValidationCodeRepository, times(1)).delete(code);
    }

    @Test
    @DisplayName("Verifica se a senha é redefinida com sucesso")
    void shouldRedefinePasswordSuccessfully() {
        User user = new User();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        emailService.redefinirSenha("test@example.com", "senha@@123");

        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Verifica se a redefinição de senha falha para usuário inexistente")
    void shouldThrowExceptionForNonExistentUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> emailService.redefinirSenha("test@example.com", "password"));
    }

    @Test
    @DisplayName("Verifica se o e-mail é validado com sucesso")
    void shouldValidateEmailSuccessfully() {
        assertDoesNotThrow(() -> emailService.validateEmail("valid@example.com"));
    }

    @Test
    @DisplayName("Verifica se o e-mail inválido lança exceção")
    void shouldThrowExceptionForInvalidEmailFormat() {
        assertThrows(NotFoundException.class, () -> emailService.validateEmail("invalid-email"));
    }

    @Test
    @DisplayName("Deve lançar CampoInvalidoException para senha inválida")
    void shouldThrowCampoInvalidoExceptionForInvalidPassword() {
        User user = new User();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(CampoInvalidoException.class, () -> emailService.redefinirSenha("test@example.com", "senhaInvalida"));
        verify(userRepository, never()).save(any());
    }
}