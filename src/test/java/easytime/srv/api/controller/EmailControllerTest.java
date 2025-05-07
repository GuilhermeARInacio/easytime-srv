package easytime.srv.api.controller;

import easytime.srv.api.infra.exceptions.CampoInvalidoException;
import easytime.srv.api.model.email.EmailRequest;
import easytime.srv.api.model.email.ValidationCode;
import easytime.srv.api.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.webjars.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

class EmailControllerTest {

    @InjectMocks
    private EmailController emailController;

    @Mock
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendEmailReturns404() {
        doThrow(new IllegalArgumentException("")).when(emailService).sendEmail(any());
        var response = emailController.sendEmail(new EmailRequest("user"));
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
    }

    @Test
    void sendEmailReturns500() {
        doThrow(new RuntimeException("")).when(emailService).sendEmail(any());
        var response = emailController.sendEmail(new EmailRequest("user"));
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
    }

    @Test
    void sendEmailReturns200() {
        doNothing().when(emailService).sendEmail(any());
        var response = emailController.sendEmail(new EmailRequest("user"));
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void whenValidateCodeReturns404ForCode() {
        doThrow(new NotFoundException("")).when(emailService).validateCode(any());
        var response = emailController.validateCode(new ValidationCode("code", "email", "senha"));
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
    }

    @Test
    void whenValidateCodeReturns401() {
        doThrow(new IllegalArgumentException("")).when(emailService).validateCode(any());
        var response = emailController.validateCode(new ValidationCode("code", "email", "senha"));
        assertEquals(HttpStatusCode.valueOf(401), response.getStatusCode());
    }

    @Test
    void whenValidateCodeReturns404ForEmail() {
        doThrow(new NotFoundException("")).when(emailService).redefinirSenha(any(), any());
        var response = emailController.validateCode(new ValidationCode("code", "email", "senha"));
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
    }

    @Test
    void whenValidateCodeReturns400ForPassword() {
        doThrow(new CampoInvalidoException("")).when(emailService).redefinirSenha(any(), any());
        var response = emailController.validateCode(new ValidationCode("code", "email", "senha"));
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    void whenValidateCodeReturns500() {
        doThrow(new RuntimeException("")).when(emailService).validateCode(any());
        var response = emailController.validateCode(new ValidationCode("code", "email", "senha"));
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
    }

    @Test
    void whenValidateCodeReturns200() {
        doNothing().when(emailService).validateCode(any());
        var response = emailController.validateCode(new ValidationCode("code", "email", "senha"));
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }
}