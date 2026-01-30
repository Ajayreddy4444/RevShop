package Exception;

/**
 * Thrown when input validation fails
 */
public class ValidationException extends AuthException {

    public ValidationException(String message) {
        super(message);
    }
}
