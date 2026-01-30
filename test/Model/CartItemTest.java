package Model;

import static org.junit.Assert.*;
import org.junit.Test;

public class CartItemTest {

    // Tests CartItem constructor and total calculation
    @Test
    public void testCartItemCreation() {

        CartItem item = new CartItem(101, "Phone", 10000, 2);

        assertEquals(101, item.getProductId());
        assertEquals("Phone", item.getProductName());
        assertEquals(10000, item.getPrice(), 0.0);
        assertEquals(2, item.getQuantity());
        assertEquals(20000, item.getTotal(), 0.0);
    }

    // Tests quantity update and recalculated total
    @Test
    public void testUpdateQuantity() {

        CartItem item = new CartItem(101, "Phone", 10000, 1);
        item.setQuantity(3);

        assertEquals(3, item.getQuantity());
        assertEquals(30000, item.getTotal(), 0.0);
    }
}
