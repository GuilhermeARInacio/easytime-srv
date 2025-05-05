//package easytime.srv.api.service;
//
//import easytime.srv.api.model.user.UserDTO;
//import easytime.srv.api.tables.User;
//import easytime.srv.api.tables.repositorys.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.when;
//
//class UserServiceTest {
//
//    @InjectMocks
//    private UserService userService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void createUserReturnUser() {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setPassword("validPassword");
//
//        when(userRepository.save(any())).thenReturn(new User());
//        var user = userService.createUser(userDTO);
//        assertEquals(User.class, user.getClass());
//    }
//
//    @Test
//    void listUsers() {
//        when(userRepository.findAll()).thenReturn(List.of(new User()));
//
//        var users = userService.listUsers();
//        assertEquals(User.class, users.get(0).getClass());
//    }
//
//    @Test
//    void getUserById() {
//        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
//        var user = userService.getUserById(1);
//        assertEquals(User.class, user.get().getClass());
//    }
//
//    @Test
//    void getUserByLogin() {
//        when(userRepository.findByLogin(any())).thenReturn(Optional.of(new User()));
//        var user = userService.getUserByLogin("");
//        assertEquals(User.class, user.get().getClass());
//    }
//
//    @Test
//    void deleteUser() {
//        userService.deleteUser(1);
//        assertDoesNotThrow(() -> userService.getUserById(1));
//    }
//}