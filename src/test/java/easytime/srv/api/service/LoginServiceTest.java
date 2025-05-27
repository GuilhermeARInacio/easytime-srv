package easytime.srv.api.service;

import easytime.srv.api.infra.security.TokenDto;
import easytime.srv.api.infra.security.TokenService;
import easytime.srv.api.model.user.DTOUsuario;
import easytime.srv.api.validacoes.Login.ValidacaoLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private ValidacaoLogin validacaoLogin;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        List<ValidacaoLogin> validacoes = List.of(validacaoLogin);
        ReflectionTestUtils.setField(loginService, "validacoes", validacoes);
        ReflectionTestUtils.setField(loginService, "tokenService", tokenService);

        when(tokenService.gerarToken(any())).thenReturn("mockedToken");
    }

    @Test
    @DisplayName("Verifica se o login retorna um token")
    void whenLoginThenReturnToken() {
        doNothing().when(validacaoLogin).validar(any());
        Authentication authentication = new UsernamePasswordAuthenticationToken("login", "senha");
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        DTOUsuario dto = new DTOUsuario("login", "senha");
        TokenDto token = loginService.login(dto);
        assertEquals(String.class, token.getClass());
    }
}