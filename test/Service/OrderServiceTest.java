package Service;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Model.CartItem;
import Exception.OrderException;

public class OrderServiceTest {

    private OrderService orderService;

    @Before
    public void setUp() {
        orderService = new OrderService();
    }

    // ✅ Empty cart should throw OrderException
    @Test(expected = OrderException.class)
    public void testCheckoutWithEmptyCart() throws OrderException {

        orderService.checkout(
                1,
                new ArrayList<CartItem>(), // empty cart
                "Hyderabad",
                "COD"
        );
    }
}
