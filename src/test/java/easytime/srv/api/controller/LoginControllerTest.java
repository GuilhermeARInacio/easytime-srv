package easytime.srv.api.controller;

import easytime.srv.api.infra.exceptions.CampoInvalidoException;
import easytime.srv.api.infra.exceptions.UsuarioESenhaNotFoundException;
import easytime.srv.api.model.DTOUsuario;
import easytime.srv.api.service.LoginService;
import easytime.srv.api.validacoes.Login.ValidacaoLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private LoginService loginService;

    private DTOUsuario usuario;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        //usuario = Mockito.mock(DTOUsuario.class);
    }

    @Test
    void shouldReturn200WhenLoginCalled() {
        when(loginService.login(any())).thenReturn("token");

        var response = loginController.login(new DTOUsuario("user", "password"));
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void shouldReturn400WhenLoginCalled() {
        doThrow(new CampoInvalidoException("")).when(loginService).login(any());
        var response = loginController.login(new DTOUsuario("user", ""));
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    void shouldReturn401WhenLoginCalled() {
        doThrow(new UsuarioESenhaNotFoundException("")).when(loginService).login(any());
        var response = loginController.login(new DTOUsuario("user", "password"));
        assertEquals(HttpStatusCode.valueOf(401), response.getStatusCode());
    }

}