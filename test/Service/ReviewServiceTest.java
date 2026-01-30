package Service;

import org.junit.Before;
import org.junit.Test;

import Exception.ReviewException;
import Exception.ValidationException;

public class ReviewServiceTest {

    private ReviewService reviewService;

    @Before
    public void setUp() {
        reviewService = new ReviewService();
    }

    // ❌ Invalid rating (greater than 5)
    @Test(expected = ValidationException.class)
    public void testInvalidRating() {
        reviewService.addReview(1, 1, 6, "Bad");
    }

    // ❌ Invalid rating (less than 1)
    @Test(expected = ValidationException.class)
    public void testRatingTooLow() {
        reviewService.addReview(1, 1, 0, "Bad");
    }

    // ❌ Review text empty
    @Test(expected = ValidationException.class)
    public void testEmptyReviewText() {
        reviewService.addReview(1, 1, 4, "");
    }

    // ❌ Buyer has not purchased product
    @Test(expected = ReviewException.class)
    public void testReviewWithoutPurchase() {
        reviewService.addReview(1, 999, 4, "Nice");
    }

    // ❌ Invalid product ID
    @Test(expected = ValidationException.class)
    public void testInvalidProductId() {
        reviewService.addReview(1, -1, 4, "Nice");
    }

    // ❌ Invalid buyer ID
    @Test(expected = ValidationException.class)
    public void testInvalidBuyerId() {
        reviewService.addReview(0, 1, 4, "Nice");
    }
}
