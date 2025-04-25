package easytime.srv.api.infra.exceptions;

public class CampoVazioException extends RuntimeException {
    public CampoVazioException(String message) {
        super(message);
    }
}
