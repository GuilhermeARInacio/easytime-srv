package easytime.srv.api.validacoes.user;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.model.user.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class UserIsValidTest {

    @InjectMocks
    private ValidacaoUser validacaoUser;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createUserDto();
    }

    @Test
    @DisplayName("Verifica se a senha é vazia no login")
    void whenCampoVazio() {
        assertThrows(InvalidUserException.class, () -> validacaoUser.validar(userDto));
    }

    void createUserDto(){
        userDTO = new UserDTO();
        userDTO.setNome("John Doe");
        userDTO.setEmail("johndoe@example.com");
        userDTO.setLogin("johndoe");
        userDTO.setPassword("password123");
        userDTO.setSector("IT");
        userDTO.setJobTitle("Developer");
        userDTO.setRole("Admin");
        userDTO.setIsActive(true);
    }
}