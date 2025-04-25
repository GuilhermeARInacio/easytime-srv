package easytime.srv.api.infra.security;

import easytime.srv.api.infra.security.SecurityFilter;
import easytime.srv.api.infra.security.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class SecurityFilterTest {

    @InjectMocks
    private SecurityFilter securityFilter;

    @Mock
    private TokenService tokenService;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateWhenTokenIsValid() throws ServletException, IOException {
        // Configura o token no cabeçalho da requisição
        String token = "validToken";
        request.addHeader("Authorization", "Bearer " + token);

        // Mock do TokenService para retornar um subject válido
        when(tokenService.getSubject(token)).thenReturn("user");

        // Executa o filtro
        securityFilter.doFilterInternal(request, response, filterChain);

        // Verifica se o contexto de segurança foi configurado
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenTokenIsInvalid() throws ServletException, IOException {
        // Configura um token inválido
        request.addHeader("Authorization", "Bearer invalidToken");

        // Mock do TokenService para retornar null
        when(tokenService.getSubject("invalidToken")).thenReturn(null);

        // Executa o filtro
        securityFilter.doFilterInternal(request, response, filterChain);

        // Verifica se o contexto de segurança não foi configurado
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenNoTokenIsProvided() throws ServletException, IOException {
        // Executa o filtro sem token
        securityFilter.doFilterInternal(request, response, filterChain);

        // Verifica se o contexto de segurança não foi configurado
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}