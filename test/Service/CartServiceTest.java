package Service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Model.Product;
import Service.CartService;

public class CartServiceTest {

    private CartService cartService;

    @Before
    public void setUp() {
        cartService = new CartService();
    }

    @Test
    public void testAddToCart() {
        Product p = new Product();
        p.setProductId(1);
        p.setName("Mouse");
        p.setPrice(500);

        cartService.addToCart(p, 2);
        assertFalse(cartService.isEmpty());
    }

    @Test
    public void testRemoveFromCart() {
        Product p = new Product();
        p.setProductId(1);
        p.setName("Mouse");
        p.setPrice(500);

        cartService.addToCart(p, 1);
        cartService.removeFromCart(1);

        assertTrue(cartService.isEmpty());
    }
}
