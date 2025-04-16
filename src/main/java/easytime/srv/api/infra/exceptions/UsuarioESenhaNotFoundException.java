package easytime.srv.api.infra.exceptions;

public class UsuarioESenhaNotFoundException extends RuntimeException {

    public UsuarioESenhaNotFoundException(String message) {
        super(message);
    }
}
