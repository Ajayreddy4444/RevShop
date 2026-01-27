package Dao;

import Model.CartItem;
import Model.OrderDetails;
import Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class OrderDAO {

    // ✅ Place Order (Checkout) with Shipping + Payment
    public int placeOrder(int buyerId, List<CartItem> cartItems, String shippingAddress, String paymentMethod) {

        String insertOrderSql =
                "INSERT INTO orders (buyer_id, total_amount, status, shipping_address, payment_method, payment_status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        String getOrderIdSql =
                "SELECT orders_seq.CURRVAL FROM dual";

        String insertItemSql =
                "INSERT INTO order_items (order_id, product_id, seller_id, quantity, price, item_total) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        String getProductSql =
                "SELECT product_id, seller_id, price, stock FROM products WHERE product_id = ?";

        String updateStockSql =
                "UPDATE products SET stock = stock - ? WHERE product_id = ? AND stock >= ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false); // ✅ transaction start

            // 1) calculate total
            double totalAmount = 0;
            for (CartItem item : cartItems) {
                totalAmount += item.getTotal();
            }

            // 2) insert into orders (including shipping + payment)
            ps = con.prepareStatement(insertOrderSql);
            ps.setInt(1, buyerId);
            ps.setDouble(2, totalAmount);
            ps.setString(3, "PLACED");
            ps.setString(4, shippingAddress);
            ps.setString(5, paymentMethod);

            // ✅ simulated payment result
            ps.setString(6, "SUCCESS");

            int rows = ps.executeUpdate();
            ps.close();
            ps = null;

            if (rows == 0) {
                con.rollback();
                return -1;
            }

            // 3) get generated order_id (sequence currval)
            int orderId = -1;

            ps = con.prepareStatement(getOrderIdSql);
            rs = ps.executeQuery();

            if (rs.next()) {
                orderId = rs.getInt(1);
            } else {
                con.rollback();
                return -1;
            }

            rs.close();
            rs = null;
            ps.close();
            ps = null;

            // 4) insert items + update stock
            for (CartItem item : cartItems) {

                int productId = item.getProductId();
                int quantity = item.getQuantity();

                int sellerId = 0;
                double price = 0;
                int stock = 0;

                // fetch product details
                ps = con.prepareStatement(getProductSql);
                ps.setInt(1, productId);
                rs = ps.executeQuery();

                if (!rs.next()) {
                    con.rollback();
                    System.out.println("❌ Product not found: " + productId);
                    return -1;
                }

                sellerId = rs.getInt("seller_id");
                price = rs.getDouble("price");
                stock = rs.getInt("stock");

                rs.close();
                rs = null;
                ps.close();
                ps = null;

                if (quantity <= 0) {
                    con.rollback();
                    System.out.println("❌ Invalid quantity for product: " + productId);
                    return -1;
                }

                if (quantity > stock) {
                    con.rollback();
                    System.out.println("❌ Not enough stock for product: " + productId);
                    return -1;
                }

                double itemTotal = price * quantity;

                // insert into order_items
                ps = con.prepareStatement(insertItemSql);
                ps.setInt(1, orderId);
                ps.setInt(2, productId);
                ps.setInt(3, sellerId);
                ps.setInt(4, quantity);
                ps.setDouble(5, price);
                ps.setDouble(6, itemTotal);
                ps.executeUpdate();

                ps.close();
                ps = null;

                // update stock
                ps = con.prepareStatement(updateStockSql);
                ps.setInt(1, quantity);
                ps.setInt(2, productId);
                ps.setInt(3, quantity);

                int updated = ps.executeUpdate();
                ps.close();
                ps = null;

                if (updated == 0) {
                    con.rollback();
                    System.out.println("❌ Stock update failed for product: " + productId);
                    return -1;
                }
            }

            con.commit(); // ✅ success
            return orderId;

        } catch (Exception e) {
            try {
                if (con != null) con.rollback();
            } catch (Exception ex) { }
            System.out.println("❌ Place Order Error: " + e.getMessage());
            return -1;

        } finally {

            try { if (rs != null) rs.close(); } catch (Exception e) { }
            try { if (ps != null) ps.close(); } catch (Exception e) { }

            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) { }
        }
    }

    // ✅ Buyer - View My Orders
    public List<OrderDetails> getOrdersByBuyerId(int buyerId) {

        List<OrderDetails> orders = new ArrayList<OrderDetails>();

        String sql =
                "SELECT o.order_id, o.buyer_id, o.status, o.created_at, " +
                "       oi.product_id, p.name AS product_name, oi.seller_id, " +
                "       oi.quantity, oi.price, oi.item_total " +
                "FROM orders o " +
                "JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "WHERE o.buyer_id = ? " +
                "ORDER BY o.order_id DESC";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, buyerId);

            rs = ps.executeQuery();
            while (rs.next()) {

                OrderDetails od = new OrderDetails();
                od.setOrderId(rs.getInt("order_id"));
                od.setBuyerId(rs.getInt("buyer_id"));
                od.setStatus(rs.getString("status"));
                od.setCreatedAt(String.valueOf(rs.getTimestamp("created_at")));

                od.setProductId(rs.getInt("product_id"));
                od.setProductName(rs.getString("product_name"));
                od.setSellerId(rs.getInt("seller_id"));
                od.setQuantity(rs.getInt("quantity"));
                od.setPrice(rs.getDouble("price"));
                od.setItemTotal(rs.getDouble("item_total"));

                orders.add(od);
            }

        } catch (Exception e) {
            System.out.println("❌ Buyer Orders Fetch Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { }
            try { if (ps != null) ps.close(); } catch (Exception e) { }
            try { if (con != null) con.close(); } catch (Exception e) { }
        }

        return orders;
    }

    // ✅ Seller - View Orders on My Products
    public List<OrderDetails> getOrdersBySellerId(int sellerId) {

        List<OrderDetails> orders = new ArrayList<OrderDetails>();

        String sql =
                "SELECT o.order_id, o.buyer_id, o.status, o.created_at, " +
                "       oi.product_id, p.name AS product_name, oi.seller_id, " +
                "       oi.quantity, oi.price, oi.item_total " +
                "FROM orders o " +
                "JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "WHERE oi.seller_id = ? " +
                "ORDER BY o.order_id DESC";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, sellerId);

            rs = ps.executeQuery();
            while (rs.next()) {

                OrderDetails od = new OrderDetails();
                od.setOrderId(rs.getInt("order_id"));
                od.setBuyerId(rs.getInt("buyer_id"));
                od.setStatus(rs.getString("status"));
                od.setCreatedAt(String.valueOf(rs.getTimestamp("created_at")));

                od.setProductId(rs.getInt("product_id"));
                od.setProductName(rs.getString("product_name"));
                od.setSellerId(rs.getInt("seller_id"));
                od.setQuantity(rs.getInt("quantity"));
                od.setPrice(rs.getDouble("price"));
                od.setItemTotal(rs.getDouble("item_total"));

                orders.add(od);
            }

        } catch (Exception e) {
            System.out.println("❌ Seller Orders Fetch Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { }
            try { if (ps != null) ps.close(); } catch (Exception e) { }
            try { if (con != null) con.close(); } catch (Exception e) { }
        }

        return orders;
    }

    // ✅ Buyer - Cancel Order
    public boolean cancelOrder(int buyerId, int orderId) {

        String checkSql =
                "SELECT status FROM orders WHERE order_id = ? AND buyer_id = ?";

        String updateOrderSql =
                "UPDATE orders SET status = 'CANCELLED' WHERE order_id = ? AND buyer_id = ?";

        String getItemsSql =
                "SELECT product_id, quantity FROM order_items WHERE order_id = ?";

        String restoreStockSql =
                "UPDATE products SET stock = stock + ? WHERE product_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // 1) Check order belongs to buyer and status is PLACED
            ps = con.prepareStatement(checkSql);
            ps.setInt(1, orderId);
            ps.setInt(2, buyerId);
            rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Order not found for this buyer!");
                con.rollback();
                return false;
            }

            String status = rs.getString("status");

            rs.close();
            rs = null;
            ps.close();
            ps = null;

            if (!"PLACED".equalsIgnoreCase(status)) {
                System.out.println("❌ Only PLACED orders can be cancelled!");
                con.rollback();
                return false;
            }

            // 2) Get all items in that order
            ps = con.prepareStatement(getItemsSql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();

            List<CartItem> items = new ArrayList<CartItem>();

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                int qty = rs.getInt("quantity");

                // using CartItem just for storing productId + qty
                items.add(new CartItem(productId, "", 0, qty));
            }

            rs.close();
            rs = null;
            ps.close();
            ps = null;

            // 3) Restore stock
            for (CartItem item : items) {
                ps = con.prepareStatement(restoreStockSql);
                ps.setInt(1, item.getQuantity());
                ps.setInt(2, item.getProductId());
                ps.executeUpdate();
                ps.close();
                ps = null;
            }

            // 4) Update order status to CANCELLED
            ps = con.prepareStatement(updateOrderSql);
            ps.setInt(1, orderId);
            ps.setInt(2, buyerId);

            int updated = ps.executeUpdate();

            if (updated == 0) {
                con.rollback();
                return false;
            }

            con.commit();
            System.out.println("✅ Order Cancelled Successfully!");
            return true;

        } catch (Exception e) {
            try {
                if (con != null) con.rollback();
            } catch (Exception ex) { }

            System.out.println("❌ Cancel Order Error: " + e.getMessage());
            return false;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { }
            try { if (ps != null) ps.close(); } catch (Exception e) { }

            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) { }
        }
    }
}
