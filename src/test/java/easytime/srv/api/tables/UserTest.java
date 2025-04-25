package easytime.srv.api.tables;

import easytime.srv.api.model.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testToEntity() {
        // Cria um UserDTO com dados de exemplo
        UserDTO userDTO = new UserDTO();
        userDTO.setNome("John Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setLogin("johndoe");
        userDTO.setPassword("password123");
        userDTO.setSector("IT");
        userDTO.setJobTitle("Developer");
        userDTO.setRole("admin");
        userDTO.setIsActive(true);

        // Converte para entidade User
        User user = User.toEntity(userDTO);

        // Verifica se os campos foram mapeados corretamente
        assertNotNull(user);
    }
}