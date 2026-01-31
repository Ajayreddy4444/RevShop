package Dao;

import Model.Product;
import Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAO {

	/**
	 * Adds a product to buyer wishlist.
	 *
	 * @param buyerId buyer ID
	 * @param productId product ID
	 * @return {@code true} if added successfully
	 */
    public boolean addToWishlist(int buyerId, int productId) {

        String sql =
            "INSERT INTO wishlist (wishlist_id, buyer_id, product_id) " +
            "VALUES (wishlist_seq.NEXTVAL, ?, ?)";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, buyerId);
            ps.setInt(2, productId);

            int rows = ps.executeUpdate();
            ps.close();
            con.close();

            return rows > 0;

        } catch (Exception e) {
            System.out.println("❌ Wishlist Add Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves wishlist items for a buyer.
     *
     * @param buyerId buyer ID
     * @return list of {@link Product}
     */

    public List<Product> getWishlistByBuyer(int buyerId) {

        List<Product> list = new ArrayList<Product>();

        String sql =
            "SELECT p.product_id, p.name, p.price, p.mrp " +
            "FROM wishlist w " +
            "JOIN products p ON w.product_id = p.product_id " +
            "WHERE w.buyer_id = ?";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, buyerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setMrp(rs.getDouble("mrp"));
                list.add(p);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println("❌ Wishlist Fetch Error: " + e.getMessage());
        }

        return list;
    }

    /**
     * Removes a product from buyer wishlist.
     *
     * @param buyerId buyer ID
     * @param productId product ID
     * @return {@code true} if removal succeeds
     */
    public boolean removeFromWishlist(int buyerId, int productId) {

        String sql =
            "DELETE FROM wishlist WHERE buyer_id = ? AND product_id = ?";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, buyerId);
            ps.setInt(2, productId);

            int rows = ps.executeUpdate();
            ps.close();
            con.close();

            return rows > 0;

        } catch (Exception e) {
            System.out.println("❌ Wishlist Remove Error: " + e.getMessage());
            return false;
        }
    }
}