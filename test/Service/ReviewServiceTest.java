package Service;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import Dao.OrderDAO;
import Dao.ReviewDAO;
import Exception.ReviewException;
import Model.Review;

public class ReviewServiceTest {

    private ReviewService reviewService;
    private ReviewDAO reviewDAOMock;
    private OrderDAO orderDAOMock;

    @Before
    public void setUp() {
        reviewDAOMock = mock(ReviewDAO.class);
        orderDAOMock = mock(OrderDAO.class);

        reviewService = new ReviewService(reviewDAOMock, orderDAOMock);
    }

    @Test
    public void testAddReviewSuccess() {
        when(orderDAOMock.hasBuyerPurchasedProduct(1, 1))
                .thenReturn(true);
        when(reviewDAOMock.hasBuyerReviewed(1, 1))
                .thenReturn(false);
        when(reviewDAOMock.addReview(any(Review.class)))
        .thenReturn(true);


        reviewService.addReview(1, 1, 5, "Good product");
    }

    @Test(expected = ReviewException.class)
    public void testAddReviewWithoutPurchase() {
        when(orderDAOMock.hasBuyerPurchasedProduct(1, 1))
                .thenReturn(false);

        reviewService.addReview(1, 1, 5, "Nice");
    }
}
