package easytime.srv.api.infra.security;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import easytime.srv.api.model.user.DTOUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "test-secret"); // Mock the secret value
    }

    @Test
    void shouldGenerateTokenSuccessfully() {
        // Arrange
        DTOUsuario usuario = mock(DTOUsuario.class);
        when(usuario.login()).thenReturn("test-user");

        // Act
        String token = tokenService.gerarToken(usuario);

        // Assert
        assertNotNull(token);
    }

    @Test
    void shouldThrowExceptionWhenGeneratingTokenFails() {
        // Arrange
        ReflectionTestUtils.setField(tokenService, "secret", null); // Simulate missing secret
        DTOUsuario usuario = mock(DTOUsuario.class);
        when(usuario.login()).thenReturn("test-user");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tokenService.gerarToken(usuario));
    }

    @Test
    void shouldGetSubjectSuccessfully() {
        // Arrange
        DTOUsuario usuario = mock(DTOUsuario.class);
        when(usuario.login()).thenReturn("test-user");
        String token = tokenService.gerarToken(usuario);

        // Act
        String subject = tokenService.getSubject(token);

        // Assert
        assertEquals("test-user", subject);
    }

    @Test
    void shouldThrowExceptionWhenTokenIsInvalid() {
        // Arrange
        String invalidToken = "invalid-token";

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tokenService.getSubject(invalidToken));
    }
}