package Model;

/**
 * Represents a product review by a buyer
 */
public class Review {

    // Review primary key
    private int reviewId;

    // Foreign keys
    private int productId;
    private int buyerId;

    // Review details
    private int rating;
    private String reviewText;

    // Created timestamp
    private String createdAt;

    // Get reviewId
    public int getReviewId() {
        return reviewId;
    }

    // Set reviewId
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    // Get productId
    public int getProductId() {
        return productId;
    }

    // Set productId
    public void setProductId(int productId) {
        this.productId = productId;
    }

    // Get buyerId
    public int getBuyerId() {
        return buyerId;
    }

    // Set buyerId
    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    // Get rating
    public int getRating() {
        return rating;
    }

    // Set rating
    public void setRating(int rating) {
        this.rating = rating;
    }

    // Get review text
    public String getReviewText() {
        return reviewText;
    }

    // Set review text
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
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
