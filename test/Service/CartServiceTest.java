package Service;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import Exception.CartException;
import Exception.InvalidCartOperationException;
import Model.Product;

public class CartServiceTest {

    private CartService cartService;

    @Before
    public void setUp() {
        cartService = new CartService();
    }

    @Test
    public void testAddToCartSuccess() {
        Product p = new Product();
        p.setProductId(1);
        p.setName("Phone");
        p.setPrice(10000);
        p.setStock(10);

        cartService.addToCart(p, 2);

        assertEquals(1, cartService.getCartItems().size());
    }

    @Test(expected = InvalidCartOperationException.class)
    public void testAddToCartInvalidQuantity() {
        Product p = new Product();
        p.setProductId(1);
        p.setStock(10);

        cartService.addToCart(p, 0);
    }

    @Test(expected = CartException.class)
    public void testViewEmptyCart() {
        cartService.viewCart();
    }
}
