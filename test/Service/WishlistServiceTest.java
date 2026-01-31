package Service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import Dao.WishlistDAO;
import Exception.WishlistException;
import Model.Product;

public class WishlistServiceTest {

    private WishlistService wishlistService;
    private WishlistDAO wishlistDAOMock;

    @Before
    public void setUp() {
        wishlistDAOMock = mock(WishlistDAO.class);
        wishlistService = new WishlistService(wishlistDAOMock);
    }

    @Test
    public void testAddToWishlistSuccess() {
        when(wishlistDAOMock.addToWishlist(1, 1))
                .thenReturn(true);

        boolean result = wishlistService.add(1, 1);

        assertTrue(result);
    }

    @Test(expected = WishlistException.class)
    public void testViewEmptyWishlist() {
    	when(wishlistDAOMock.getWishlistByBuyer(1))
        .thenReturn(new ArrayList<Product>());



        wishlistService.view(1);
    }
}
