package Model;

import static org.junit.Assert.*;
import org.junit.Test;

public class ReviewTest {

    // Tests Review model fields
    @Test
    public void testReviewModel() {

        Review r = new Review();
        r.setReviewId(1);
        r.setProductId(101);
        r.setBuyerId(5);
        r.setRating(4);
        r.setReviewText("Good product");
        r.setCreatedAt("2026-01-01");

        assertEquals(1, r.getReviewId());
        assertEquals(101, r.getProductId());
        assertEquals(5, r.getBuyerId());
        assertEquals(4, r.getRating());
        assertEquals("Good product", r.getReviewText());
        assertEquals("2026-01-01", r.getCreatedAt());
    }
}
