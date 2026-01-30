package Service;

import Dao.OrderDAO;
import Dao.ReviewDAO;
import Exception.ReviewException;
import Exception.ValidationException;
import Model.Review;

import java.util.List;

public class ReviewService {

    private ReviewDAO reviewDAO = new ReviewDAO();
    private OrderDAO orderDAO = new OrderDAO();

    // ===============================
    // ADD REVIEW
    // ===============================
    public void addReview(int buyerId, int productId, int rating, String reviewText) {

        if (buyerId <= 0 || productId <= 0) {
            throw new ValidationException("Invalid buyer or product ID");
        }

        if (rating < 1 || rating > 5) {
            throw new ValidationException("Rating must be between 1 and 5");
        }

        if (reviewText == null || reviewText.trim().isEmpty()) {
            throw new ValidationException("Review text cannot be empty");
        }

        // Buyer must have purchased the product
        if (!orderDAO.hasBuyerPurchasedProduct(buyerId, productId)) {
            throw new ReviewException("You can review only purchased products");
        }

        // Prevent duplicate reviews
        if (reviewDAO.hasBuyerReviewed(buyerId, productId)) {
            throw new ReviewException("You already reviewed this product");
        }

        Review review = new Review();
        review.setBuyerId(buyerId);
        review.setProductId(productId);
        review.setRating(rating);
        review.setReviewText(reviewText);

        boolean saved = reviewDAO.addReview(review);

        if (!saved) {
            throw new ReviewException("Failed to add review");
        }
    }

    // ===============================
    // VIEW REVIEWS BY PRODUCT
    // ===============================
    public List<Review> viewReviewsByProduct(int productId) {

        if (productId <= 0) {
            throw new ValidationException("Invalid product ID");
        }

        return reviewDAO.getReviewsByProductId(productId);
    }

    // ===============================
    // VIEW REVIEWS FOR SELLER
    // ===============================
    public List<Review> viewReviewsForSeller(int sellerId) {

        if (sellerId <= 0) {
            throw new ValidationException("Invalid seller ID");
        }

        return reviewDAO.getReviewsBySellerId(sellerId);
    }
}
