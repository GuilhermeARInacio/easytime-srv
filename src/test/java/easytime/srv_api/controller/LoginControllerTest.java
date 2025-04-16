package easytime.srv_api.controller;

import easytime.srv.api.controller.LoginController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginControllerTest {

//    @InjectMocks
    private final LoginController loginController = new LoginController();

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturn200WhenLoginCalled() {


//        var response = loginController.login(new DTOUsuario("user", "password"));
//        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void shouldReturn401WhenLoginCalled() {

    }

    @Test
    void hello() {
    }
}