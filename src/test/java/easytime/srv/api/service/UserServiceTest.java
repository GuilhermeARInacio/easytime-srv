package easytime.srv.api.service;

import easytime.srv.api.validacoes.user.ValidacaoUser;
import easytime.srv.api.model.user.UserDTO;
import easytime.srv.api.tables.User;
import easytime.srv.api.tables.repositorys.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private List<ValidacaoUser> validacoes;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createUserDto();
    }

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    void shouldCreateUserSuccessfully() {
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(userDTO);

        assertEquals(user, createdUser);
    }

    @Test
    @DisplayName("Deve listar os usuários com sucesso")
    void listUsers() {
        when(userRepository.findAll()).thenReturn(List.of(new User()));

        var users = userService.listUsers();
        assertEquals(User.class, users.get(0).getClass());
    }

    @Test
    @DisplayName("Deve listar um usuário pelo id com sucesso")
    void getUserById() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        var user = userService.getUserById(1);
        assertEquals(User.class, user.get().getClass());
    }

    @Test
    @DisplayName("Deve listar um usuário pelo login com sucesso")
    void getUserByLogin() {
        when(userRepository.findByLogin(any())).thenReturn(Optional.of(new User()));
        var user = userService.getUserByLogin("");
        assertEquals(User.class, user.get().getClass());
    }

    @Test
    @DisplayName("Deve deletar um usuário com sucesso")
    void deleteUser() {
        userService.deleteUser(1);
        assertDoesNotThrow(() -> userService.getUserById(1));
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