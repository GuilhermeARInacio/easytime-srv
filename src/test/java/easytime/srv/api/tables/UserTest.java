package easytime.srv.api.tables;

import easytime.srv.api.model.user.UserDTO;
import easytime.srv.api.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserTest {

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setNome("Fulano");
        userDTO.setEmail("fulano@example.com");
        userDTO.setLogin("fulanouser");
        userDTO.setPassword("senha123");
        userDTO.setSector("RH");
        userDTO.setJobTitle("Analista");
        userDTO.setRole("admin");
        userDTO.setIsActive(true);
    }

    @Test
    void testToEntityMapsFieldsCorrectly() {
        // Instead of encrypting, return a fake hash for testing
        try (MockedStatic<PasswordUtil> utilities = Mockito.mockStatic(PasswordUtil.class)) {
            utilities.when(() -> PasswordUtil.encryptPassword("senha123"))
                    .thenReturn("encryptedSenha");

            User user = User.toEntity(userDTO);

            assertEquals(userDTO.getNome(), user.getNome());
            assertEquals(userDTO.getEmail(), user.getEmail());
            assertEquals(userDTO.getLogin(), user.getLogin());
            assertEquals("encryptedSenha", user.getPassword());
            assertEquals(userDTO.getSector(), user.getSector());
            assertEquals(userDTO.getJobTitle(), user.getJobTitle());
            assertEquals(userDTO.getRole(), user.getRole());
            assertEquals(userDTO.getIsActive(), user.getIsActive());

            // Check timestamps are initialized
            assertNotNull(user.getCreationDate());
            assertNotNull(user.getLastLogin());
            assertNotNull(user.getUpdateDate());
        }
    }

    @Test
    void testDefaultValues() {
        User user = new User();
        assertEquals("user", user.getRole());
        assertTrue(user.getIsActive());
    }

    // You could add other tests for setters/getters if needed, or check nullability for minimal constructors etc.
}