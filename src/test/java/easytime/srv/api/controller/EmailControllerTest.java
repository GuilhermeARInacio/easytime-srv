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
import org.springframework.http.ResponseEntity;
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
    void sendEmailShouldReturn404WhenEmailNotFound() {
        doThrow(new NotFoundException("Email não encontrado")).when(emailService).sendEmail(any());
        var response = emailController.sendEmail(new EmailRequest("user@example.com"));

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Email não encontrado", response.getBody());
    }

    @Test
    void sendEmailShouldReturn500OnUnexpectedError() {
        doThrow(new RuntimeException("Erro interno")).when(emailService).sendEmail(any());
        var response = emailController.sendEmail(new EmailRequest("user@example.com"));

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Erro interno"));
    }

    @Test
    void sendEmailShouldReturn200OnSuccess() {
        doNothing().when(emailService).sendEmail(any());
        var response = emailController.sendEmail(new EmailRequest("user@example.com"));

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Email enviado, verifique sua caixa de entrada ou spam.", response.getBody());
    }

    @Test
    void validateCodeShouldReturn404WhenCodeNotFound() {
        doThrow(new NotFoundException("Código não encontrado")).when(emailService).validateCode(any());
        var response = emailController.validateCode(new ValidationCode("invalid-code", "user@example.com", "password"));

        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Código não encontrado"));
    }

    @Test
    void validateCodeShouldReturn401WhenCodeIsInvalid() {
        doThrow(new IllegalArgumentException("Código inválido")).when(emailService).validateCode(any());
        var response = emailController.validateCode(new ValidationCode("invalid-code", "user@example.com", "password"));

        assertEquals(401, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Código inválido"));
    }

    @Test
    void validateCodeShouldReturn404WhenEmailNotFound() {
        doThrow(new NotFoundException("Email não encontrado")).when(emailService).redefinirSenha(any(), any());
        var response = emailController.validateCode(new ValidationCode("valid-code", "user@example.com", "password"));

        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Email não encontrado"));
    }

    @Test
    void validateCodeShouldReturn400WhenPasswordIsInvalid() {
        doThrow(new CampoInvalidoException("Senha inválida")).when(emailService).redefinirSenha(any(), any());
        var response = emailController.validateCode(new ValidationCode("valid-code", "user@example.com", "invalid-password"));

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Senha inválida"));
    }

    @Test
    void validateCodeShouldReturn500OnUnexpectedError() {
        doThrow(new RuntimeException("Erro interno")).when(emailService).validateCode(any());
        var response = emailController.validateCode(new ValidationCode("valid-code", "user@example.com", "password"));

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Erro interno"));
    }

    @Test
    void validateCodeShouldReturn200OnSuccess() {
        doNothing().when(emailService).validateCode(any());
        doNothing().when(emailService).redefinirSenha(any(), any());
        var response = emailController.validateCode(new ValidationCode("valid-code", "user@example.com", "password"));

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Senha redefinida com sucesso.", response.getBody());
    }
}