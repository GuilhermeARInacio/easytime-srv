package easytime.srv.api.service;

import easytime.srv.api.infra.security.TokenDto;
import easytime.srv.api.infra.security.TokenService;
import easytime.srv.api.model.user.DTOUsuario;
import easytime.srv.api.tables.User;
import easytime.srv.api.tables.repositorys.UserRepository;
import easytime.srv.api.validacoes.login.ValidacaoLogin;
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

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private ValidacaoLogin validacaoLogin;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

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
    @DisplayName("Verifica se o userLogin retorna um token")
    void whenLoginThenReturnToken() {
        DTOUsuario dto = new DTOUsuario("userLogin", "senha");
        User user = mock(User.class);

        doNothing().when(validacaoLogin).validar(any());
        Authentication authentication = new UsernamePasswordAuthenticationToken("userLogin", "senha");
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByLogin(dto.login())).thenReturn(Optional.ofNullable(user));
        when(user.getRole()).thenReturn("ROLE_USER");

        TokenDto token = loginService.login(dto);
        assertEquals(TokenDto.class, token.getClass());
    }
}