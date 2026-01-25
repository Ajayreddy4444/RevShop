package Dao;

import Model.Product;
import Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // ✅ Add product (Seller)
    public boolean addProduct(Product product) {

        String sql = "INSERT INTO products(seller_id, name, description, category, mrp, price, stock) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, product.getSellerId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setString(4, product.getCategory());
            ps.setDouble(5, product.getMrp());
            ps.setDouble(6, product.getPrice());
            ps.setInt(7, product.getStock());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Add Product Error: " + e.getMessage());
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return false;
    }

    // ✅ View all products (Buyer)
    public List<Product> getAllProducts() {

        List<Product> products = new ArrayList<Product>();
        String sql = "SELECT * FROM products ORDER BY product_id";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setSellerId(rs.getInt("seller_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setCategory(rs.getString("category"));
                p.setMrp(rs.getDouble("mrp"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));

                products.add(p);
            }

        } catch (Exception e) {
            System.out.println("Fetch Products Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return products;
    }

    public List<Product> getProductsByCategory(String category) {

        List<Product> products = new ArrayList<Product>();
        String sql = "SELECT * FROM products WHERE LOWER(category) = LOWER(?) ORDER BY product_id";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, category);

            rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setSellerId(rs.getInt("seller_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setCategory(rs.getString("category"));
                p.setMrp(rs.getDouble("mrp"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));

                products.add(p);
            }

        } catch (Exception e) {
            System.out.println("Category Search Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return products;
    }

    public List<Product> searchProductsByKeyword(String keyword) {

        List<Product> products = new ArrayList<Product>();
        String sql = "SELECT * FROM products WHERE LOWER(name) LIKE LOWER(?) ORDER BY product_id";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setSellerId(rs.getInt("seller_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setCategory(rs.getString("category"));
                p.setMrp(rs.getDouble("mrp"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));

                products.add(p);
            }

        } catch (Exception e) {
            System.out.println("Keyword Search Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return products;
    }

    public List<String> getAllCategories() {

        List<String> categories = new ArrayList<String>();
        String sql = "SELECT DISTINCT category FROM products ORDER BY category";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }

        } catch (Exception e) {
            System.out.println("Fetch Categories Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return categories;
    }

    // ✅ View products added by a particular seller
    public List<Product> getProductsBySellerId(int sellerId) {

        List<Product> products = new ArrayList<Product>();
        String sql = "SELECT * FROM products WHERE seller_id = ? ORDER BY product_id";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, sellerId);

            rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setSellerId(rs.getInt("seller_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setCategory(rs.getString("category"));
                p.setMrp(rs.getDouble("mrp"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));

                products.add(p);
            }

        } catch (Exception e) {
            System.out.println("Seller Products Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return products;
    }

    public Product getProductById(int productId) {

        String sql = "SELECT * FROM products WHERE product_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, productId);

            rs = ps.executeQuery();

            if (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setSellerId(rs.getInt("seller_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setCategory(rs.getString("category"));
                p.setMrp(rs.getDouble("mrp"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));

                return p;
            }

        } catch (Exception e) {
            System.out.println("Fetch Product By ID Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return null;
    }
}
