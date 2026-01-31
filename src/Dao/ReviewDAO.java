package Dao;

import Model.Review;
import Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

	/**
	 * Inserts a new product review.
	 *
	 * @param review the review details
	 * @return {@code true} if insertion succeeds
	 */
	public boolean addReview(Review review) {

        String sql =
            "INSERT INTO product_reviews (review_id, product_id, buyer_id, rating, review_text) " +
            "VALUES (product_reviews_seq.NEXTVAL, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, review.getProductId());
            ps.setInt(2, review.getBuyerId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getReviewText());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Add Review Error: " + e.getMessage());
            return false;

        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

	/**
	 * Retrieves all reviews for a specific product.
	 *
	 * @param productId product ID
	 * @return list of {@link Review}
	 */
	public List<Review> getReviewsByProductId(int productId) {

        List<Review> reviews = new ArrayList<Review>();

        String sql =
            "SELECT review_id, product_id, buyer_id, rating, review_text, created_at " +
            "FROM product_reviews WHERE product_id = ? ORDER BY created_at DESC";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, productId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Review r = new Review();
                r.setReviewId(rs.getInt("review_id"));
                r.setProductId(rs.getInt("product_id"));
                r.setBuyerId(rs.getInt("buyer_id"));
                r.setRating(rs.getInt("rating"));
                r.setReviewText(rs.getString("review_text"));
                r.setCreatedAt(String.valueOf(rs.getTimestamp("created_at")));
                reviews.add(r);
            }

        } catch (Exception e) {
            System.out.println("Fetch Reviews Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return reviews;
    }

	/**
	 * Checks if a buyer has already reviewed a product.
	 *
	 * @param buyerId buyer ID
	 * @param productId product ID
	 * @return {@code true} if review exists
	 */
	public boolean hasBuyerReviewed(int buyerId, int productId) {

        String sql =
            "SELECT COUNT(*) FROM product_reviews WHERE buyer_id = ? AND product_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, buyerId);
            ps.setInt(2, productId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            System.out.println("Review Check Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return false;
    }

	/**
	 * Retrieves all reviews for products sold by a seller.
	 *
	 * @param sellerId seller ID
	 * @return list of {@link Review}
	 */    
	public List<Review> getReviewsBySellerId(int sellerId) {

        List<Review> reviews = new ArrayList<Review>();

        String sql =
            "SELECT r.review_id, r.product_id, r.buyer_id, r.rating, r.review_text, r.created_at " +
            "FROM product_reviews r " +
            "JOIN products p ON r.product_id = p.product_id " +
            "WHERE p.seller_id = ? " +
            "ORDER BY r.created_at DESC";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, sellerId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Review r = new Review();
                r.setReviewId(rs.getInt("review_id"));
                r.setProductId(rs.getInt("product_id"));
                r.setBuyerId(rs.getInt("buyer_id"));
                r.setRating(rs.getInt("rating"));
                r.setReviewText(rs.getString("review_text"));
                r.setCreatedAt(String.valueOf(rs.getTimestamp("created_at")));
                reviews.add(r);
            }

        } catch (Exception e) {
            System.out.println("Seller Reviews Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return reviews;
    }
}
