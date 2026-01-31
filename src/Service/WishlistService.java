package Service;

import Dao.WishlistDAO;
import Exception.ValidationException;
import Exception.WishlistException;
import Model.Product;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Manages buyer wishlist operations.
 */
public class WishlistService {

    private static final Logger logger =
            Logger.getLogger(WishlistService.class);

    private WishlistDAO wishlistDAO;

    /**
     * Default constructor.
     */
    public WishlistService() {
        this.wishlistDAO = new WishlistDAO();
    }

    /**
     * Constructor for Mockito testing.
     */
    public WishlistService(WishlistDAO wishlistDAO) {
        this.wishlistDAO = wishlistDAO;
    }

    /**
     * Adds a product to buyer wishlist.
     */
    public boolean add(int buyerId, int productId) {

        if (buyerId <= 0 || productId <= 0) {
            throw new ValidationException("Invalid buyer or product ID");
        }

        boolean added = wishlistDAO.addToWishlist(buyerId, productId);

        if (!added) {
            throw new WishlistException("Unable to add product to wishlist");
        }

        return true;
    }

    /**
     * Retrieves buyer wishlist.
     */
    public List<Product> view(int buyerId) {

        if (buyerId <= 0) {
            throw new ValidationException("Invalid buyer ID");
        }

        List<Product> wishlist = wishlistDAO.getWishlistByBuyer(buyerId);

        if (wishlist == null || wishlist.isEmpty()) {
            throw new WishlistException("Wishlist is empty");
        }

        return wishlist;
    }

    /**
     * Removes a product from wishlist.
     */
    public void remove(int buyerId, int productId) {

        if (buyerId <= 0 || productId <= 0) {
            throw new ValidationException("Invalid buyer or product ID");
        }

        boolean removed =
                wishlistDAO.removeFromWishlist(buyerId, productId);

        if (!removed) {
            throw new WishlistException("Product not found in wishlist");
        }
    }
}
