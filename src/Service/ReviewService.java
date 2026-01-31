package Service;

import Dao.OrderDAO;
import Dao.ReviewDAO;
import Exception.ReviewException;
import Exception.ValidationException;
import Model.Review;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Handles product review operations.
 */
public class ReviewService {

    private static final Logger logger =
            Logger.getLogger(ReviewService.class);

    private ReviewDAO reviewDAO;
    private OrderDAO orderDAO;

    /**
     * Default constructor.
     */
    public ReviewService() {
        this.reviewDAO = new ReviewDAO();
        this.orderDAO = new OrderDAO();
    }

    /**
     * Constructor for Mockito testing.
     */
    public ReviewService(ReviewDAO reviewDAO, OrderDAO orderDAO) {
        this.reviewDAO = reviewDAO;
        this.orderDAO = orderDAO;
    }

    /**
     * Adds a review for a purchased product.
     */
    public void addReview(int buyerId, int productId,
                          int rating, String reviewText) {

        if (buyerId <= 0 || productId <= 0) {
            throw new ValidationException("Invalid buyer or product ID");
        }

        if (rating < 1 || rating > 5) {
            throw new ValidationException("Rating must be between 1 and 5");
        }

        if (reviewText == null || reviewText.trim().isEmpty()) {
            throw new ValidationException("Review text cannot be empty");
        }

        if (!orderDAO.hasBuyerPurchasedProduct(buyerId, productId)) {
            throw new ReviewException("You must purchase the product before reviewing");
        }

        if (reviewDAO.hasBuyerReviewed(buyerId, productId)) {
            throw new ReviewException("You have already reviewed this product");
        }

        Review review = new Review();
        review.setBuyerId(buyerId);
        review.setProductId(productId);
        review.setRating(rating);
        review.setReviewText(reviewText);

        boolean added = reviewDAO.addReview(review);

        if (!added) {
            throw new ReviewException("Failed to add review");
        }
    }

    /**
     * Retrieves reviews for a product.
     */
    public List<Review> viewReviewsByProduct(int productId) {

        if (productId <= 0) {
            throw new ValidationException("Invalid product ID");
        }

        return reviewDAO.getReviewsByProductId(productId);
    }

    /**
     * Retrieves reviews for a seller.
     */
    public List<Review> viewReviewsForSeller(int sellerId) {

        if (sellerId <= 0) {
            throw new ValidationException("Invalid seller ID");
        }

        return reviewDAO.getReviewsBySellerId(sellerId);
    }
}
