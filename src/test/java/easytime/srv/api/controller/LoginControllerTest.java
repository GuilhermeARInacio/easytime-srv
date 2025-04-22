package easytime.srv.api.controller;

import easytime.srv.api.model.DTOUsuario;
import easytime.srv.api.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class LoginControllerTest {

    @InjectMocks
    private final LoginController loginController = new LoginController();

    @Mock
    private LoginService loginService;

    private DTOUsuario usuario;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        usuario = Mockito.mock(DTOUsuario.class);
    }

    @Test
    void shouldReturn200WhenLoginCalled() {
        when(loginService.login(any())).thenReturn("token");

        var response = loginController.login(new DTOUsuario("user", "password"));
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void shouldReturn401WhenLoginCalled() {
        when(usuario.senha()).thenReturn(null);
        when(usuario.usuario()).thenReturn("usuario");

        var response = loginController.login(new DTOUsuario("user", "password"));
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());

    }

}