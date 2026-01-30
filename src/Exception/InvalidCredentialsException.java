package Exception;

/**
 * Thrown when login credentials are invalid
 */
public class InvalidCredentialsException extends AuthException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
