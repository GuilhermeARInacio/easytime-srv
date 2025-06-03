package easytime.srv.api.validacoes.user;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.infra.exceptions.ObjectAlreadyExistsException;
import easytime.srv.api.model.user.UserDTO;
import easytime.srv.api.tables.User;
import easytime.srv.api.tables.repositorys.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class ValidacaoUsuarioExisteTest {

    @InjectMocks
    private ValidacaoUsuarioExiste validacaoUser;

    @Mock
    private UserRepository userRepository;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createUserDto();
    }

    @Test
    @DisplayName("Usuário é inválido quando email já existe")
    void whenUserEmailExists() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
        assertThrows(ObjectAlreadyExistsException.class, () -> validacaoUser.validar(userDTO));
    }

    @Test
    @DisplayName("Usuário é inválido quando userLogin já existe")
    void whenUserLoginExists() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(any())).thenReturn(Optional.of(new User()));
        assertThrows(ObjectAlreadyExistsException.class, () -> validacaoUser.validar(userDTO));
    }

    @Test
    @DisplayName("Usuário é válido quando é um usuário novo")
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