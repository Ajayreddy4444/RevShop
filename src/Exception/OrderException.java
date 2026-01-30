package Exception;

/**
 * Used for all order-related business errors
 */
public class OrderException extends RuntimeException  {

    public OrderException(String message) {
        super(message);
    }
}
