package easytime.srv_api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public enum Usuario {

    USUARIO1(1L, "abc@gmail.com", "1234"),
    USUARIO2(2L, "abcd@gmail.com", "12345");

    private final Long id;
    private final String login;
    private final String senha;

    public static Usuario buscarPorLoginESenha(String username, String senha) {
        for (Usuario usuario : Usuario.values()) {
            if (usuario.getLogin().equalsIgnoreCase(username) && usuario.getSenha().equals(senha)) {
                return usuario;
            }
        }
        return null;
    }
}
