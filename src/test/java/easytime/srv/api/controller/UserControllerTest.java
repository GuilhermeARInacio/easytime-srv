package easytime.srv.api.controller;

import easytime.srv.api.model.UserDTO;
import easytime.srv.api.service.UserService;
import easytime.srv.api.tables.User;
import org.apache.coyote.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserDTO userDTO;

    @Mock
    private List<User> users;

    @Mock
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Checa se criar usuário retorna 400")
    void whenCreateUserNotValid() {
        when(userDTO.isValid()).thenReturn(false);
        var response = userController.createUser(userDTO);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    @DisplayName("Checa se criar usuário retorna 201")
    void whenCreateUserCreates() {
        when(userDTO.isValid()).thenReturn(true);
        var response = userController.createUser(userDTO);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
    }

    @Test
    @DisplayName("Checa se listar usuário retorna 404")
    void whenListUsersNotFound() {
        when(users.isEmpty()).thenReturn(true);
        var response = userController.listUsers();
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
    }

    @Test
    @DisplayName("Checa se listar usuário retorna 200")
    void whenListUsersOk() {
        when(userService.listUsers()).thenReturn(List.of(new User()));
        var response = userController.listUsers();
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    @DisplayName("Checa se listar usuário pelo id retorna 404")
    void whenGetUserByIdNotFound() {
        when(userService.getUserById(1)).thenReturn(Optional.empty());
        var response = userController.getUserById(1);
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
    }

    @Test
    @DisplayName("Checa se listar usuário pelo id retorna 200")
    void whenGetUserByIdOk() {
        when(userService.getUserById(1)).thenReturn(Optional.of(user));
        var response = userController.getUserById(1);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void whenDeleteNotFound() {
        when(userService.getUserById(1)).thenReturn(Optional.empty());
        var response = userController.delete(1);
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
    }

    @Test
    void whenDeleteOk() {
        when(userService.getUserById(1)).thenReturn(Optional.of(user));
        var response = userController.delete(1);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }
}