package easytime.srv.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class UserDTO {
    private String nome;
    private String email;
    private String login;
    private String password;
    private String sector;
    private String jobTitle;
    private String role;
    private Boolean isActive;

    public boolean isValid() {
        return nome != null && !nome.isEmpty() &&
                email != null && !email.isEmpty() &&
                login != null && !login.isEmpty() &&
                password != null && !password.isEmpty() &&
                sector != null && !sector.isEmpty() &&
                jobTitle != null && !jobTitle.isEmpty() &&
                role != null && !role.isEmpty() &&
                isActive != null;
    }
}