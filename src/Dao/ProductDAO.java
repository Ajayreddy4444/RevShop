package Dao;

import Exception.ProductException;
import Model.Product;
import Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Adds a new product for a seller
    public boolean addProduct(Product product) {

        String sql =
            "INSERT INTO products (seller_id, name, description, category, mrp, price, stock) " +
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
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    // Fetches all products
    public List<Product> getAllProducts() {

        List<Product> products = new ArrayList<Product>();
        String sql = "SELECT * FROM products WHERE is_active = 'Y' ORDER BY product_id";

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

    // Fetches products by category
    public List<Product> getProductsByCategory(String category) {

        List<Product> products = new ArrayList<Product>();
        String sql = "SELECT * FROM products WHERE LOWER(category) = LOWER(?) AND is_active = 'Y'";

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
            System.out.println("Category Fetch Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return products;
    }

    // Searches products using keyword
    public List<Product> searchProductsByKeyword(String keyword) {

        List<Product> products = new ArrayList<Product>();
        String sql = "SELECT * FROM products WHERE LOWER(name) LIKE LOWER(?) AND is_active = 'Y'";

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
            System.out.println("Search Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return products;
    }

    // Fetches all unique product categories
    public List<String> getAllCategories() {

        List<String> categories = new ArrayList<String>();
        String sql = "SELECT DISTINCT category FROM products WHERE is_active = 'Y'";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Category List Error: " + e.getMessage());
        }

        return categories;
    }

    // Fetches products added by a specific seller
    public List<Product> getProductsBySellerId(int sellerId) {

        List<Product> products = new ArrayList<Product>();
        String sql = "SELECT * FROM products WHERE seller_id = ? AND is_active = 'Y'";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, sellerId);
            ResultSet rs = ps.executeQuery();

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

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Seller Products Error: " + e.getMessage());
        }

        return products;
    }

    // Fetches a product by product ID
    public Product getProductById(int productId) {

        String sql = "SELECT * FROM products WHERE product_id = ? AND is_active = 'Y'";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

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

                rs.close();
                ps.close();
                con.close();
                return p;
            }

        } catch (Exception e) {
            System.out.println("Product Fetch Error: " + e.getMessage());
        }

        return null;
    }

    // Updates product details by seller
    public boolean updateProduct(Product product) {

        String sql =
            "UPDATE products SET name=?, description=?, category=?, mrp=?, price=?, stock=? " +
            "WHERE product_id=? AND seller_id=?";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getCategory());
            ps.setDouble(4, product.getMrp());
            ps.setDouble(5, product.getPrice());
            ps.setInt(6, product.getStock());
            ps.setInt(7, product.getProductId());
            ps.setInt(8, product.getSellerId());

            int rows = ps.executeUpdate();
            ps.close();
            con.close();

            return rows > 0;

        } catch (Exception e) {
            System.out.println("Update Product Error: " + e.getMessage());
            return false;
        }
    }

    // Deletes a product by seller
    

    public boolean deleteProduct(int productId, int sellerId) {

        String sql = "DELETE FROM products WHERE product_id = ? AND seller_id = ?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, productId);
            ps.setInt(2, sellerId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            // ORA-02292 → child record exists (FK violation)
            if (e.getErrorCode() == 2292) {
                throw new RuntimeException("FK_CONSTRAINT");
            }

            throw new RuntimeException("DB_ERROR");

        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
    
    public boolean deactivateProduct(int productId, int sellerId) {

        String sql =
            "UPDATE products SET is_active = 'N' " +
            "WHERE product_id = ? AND seller_id = ?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, productId);
            ps.setInt(2, sellerId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Deactivate Product Error: " + e.getMessage());
            return false;

        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
    
    public boolean isActiveProduct(int productId) {

        String sql = "SELECT COUNT(*) FROM products WHERE product_id = ? AND is_active = 'Y'";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, productId);

            ResultSet rs = ps.executeQuery();
            rs.next();

            boolean exists = rs.getInt(1) > 0;

            rs.close();
            ps.close();
            con.close();

            return exists;

        } catch (Exception e) {
            return false;
        }
    }




}
