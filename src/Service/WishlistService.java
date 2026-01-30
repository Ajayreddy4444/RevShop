package Service;

import Dao.WishlistDAO;
import Model.Product;

import java.util.List;

public class WishlistService {

    private WishlistDAO wishlistDAO = new WishlistDAO();

    public boolean add(int buyerId, int productId) {
        return wishlistDAO.addToWishlist(buyerId, productId);
    }

    public List<Product> view(int buyerId) {
        return wishlistDAO.getWishlistByBuyer(buyerId);
    }

    public boolean remove(int buyerId, int productId) {
        return wishlistDAO.removeFromWishlist(buyerId, productId);
    }
}