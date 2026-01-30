package Model;

/**
 * Represents a wishlist entry for a buyer
 */
public class Wishlist {

    // Wishlist primary key
    private int wishlistId;

    // Buyer and product references
    private int buyerId;
    private int productId;

    // Created timestamp
    private String createdAt;

    // Get wishlistId
    public int getWishlistId() {
        return wishlistId;
    }

    // Set wishlistId
    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }

    // Get buyerId
    public int getBuyerId() {
        return buyerId;
    }

    // Set buyerId
    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    // Get productId
    public int getProductId() {
        return productId;
    }

    // Set productId
    public void setProductId(int productId) {
        this.productId = productId;
    }

    // Get createdAt
    public String getCreatedAt() {
        return createdAt;
    }

    // Set createdAt
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
