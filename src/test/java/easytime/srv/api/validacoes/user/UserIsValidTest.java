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
    private UserIsValid validacaoUser;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createUserDto();
    }

    @Test
    @DisplayName("Usuário é inválido quando um campo está faltando")
    void whenUserNotValid() {
        userDTO.setPassword("");
        assertThrows(InvalidUserException.class, () -> validacaoUser.validar(userDTO));
    }

    @Test
    @DisplayName("Usuário é válido quando todos os campos estão presentes")
    void whenUserIsValid() {
        assertDoesNotThrow(() -> validacaoUser.validar(userDTO));
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