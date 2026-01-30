package Model;

import static org.junit.Assert.*;
import org.junit.Test;

public class WishlistTest {

    // Tests Wishlist model getters and setters
    @Test
    public void testWishlistModel() {

        Wishlist w = new Wishlist();
        w.setWishlistId(1);
        w.setBuyerId(10);
        w.setProductId(100);
        w.setCreatedAt("2026-01-01");

        assertEquals(1, w.getWishlistId());
        assertEquals(10, w.getBuyerId());
        assertEquals(100, w.getProductId());
        assertEquals("2026-01-01", w.getCreatedAt());
    }
}
