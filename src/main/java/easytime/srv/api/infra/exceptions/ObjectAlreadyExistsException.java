package easytime.srv.api.infra.exceptions;

public class ObjectAlreadyExistsException extends RuntimeException {
    public ObjectAlreadyExistsException(String message){
        super(message);
    }
}
