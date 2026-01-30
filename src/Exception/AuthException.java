package Exception;

/**
 * Base exception for all authentication related errors
 */
public class AuthException extends RuntimeException  {

    public AuthException(String message) {
        super(message);
    }
}
