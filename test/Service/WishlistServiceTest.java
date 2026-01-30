package Service;

import org.junit.Before;
import org.junit.Test;

import Exception.ValidationException;

public class WishlistServiceTest {

    private WishlistService wishlistService;

    @Before
    public void setUp() {
        wishlistService = new WishlistService();
    }

    // ❌ Invalid product ID while adding
    @Test(expected = ValidationException.class)
    public void testAddInvalidProduct() {
        wishlistService.add(1, -10);
    }

    // ❌ Invalid user ID while adding
    @Test(expected = ValidationException.class)
    public void testAddInvalidUser() {
        wishlistService.add(0, 5);
    }

    // ❌ Invalid product ID while removing
    @Test(expected = ValidationException.class)
    public void testRemoveInvalidProduct() {
        wishlistService.remove(1, -5);
    }

    // ❌ Invalid user ID while removing
    @Test(expected = ValidationException.class)
    public void testRemoveInvalidUser() {
        wishlistService.remove(0, 5);
    }
}
