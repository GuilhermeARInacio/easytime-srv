package easytime.srv.api.infra.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;

class CustomAuthenticationProviderTest {

    private final CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider();

    @Test
    void shouldAuthenticateSuccessfully() {
        // Arrange
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password");

        // Act
        Authentication result = authenticationProvider.authenticate(authentication);

        // Assert
        assertNotNull(result);
        assertEquals("user", result.getName());
        assertNull(result.getCredentials());
        assertTrue(result.getAuthorities().isEmpty());
    }

    @Test
    void shouldSupportUsernamePasswordAuthenticationToken() {
        // Act
        boolean supports = authenticationProvider.supports(UsernamePasswordAuthenticationToken.class);

        // Assert
        assertTrue(supports);
    }

    @Test
    void shouldNotSupportOtherAuthenticationTypes() {
        // Act
        boolean supports = authenticationProvider.supports(Object.class);

        // Assert
        assertFalse(supports);
    }
}