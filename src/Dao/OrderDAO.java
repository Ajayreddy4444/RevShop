package Dao;

import Model.CartItem;
import Model.OrderDetails;
import Service.NotificationService;
import Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class OrderDAO {

    private NotificationService notificationService = new NotificationService();

    // =========================================================
    // ✅ PLACE ORDER (Shipping + Payment + Notifications)
    // =========================================================
    public int placeOrder(int buyerId, List<CartItem> cartItems,
                          String shippingAddress, String paymentMethod) {

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

        List<Integer> sellerIds = new ArrayList<Integer>();

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // 1️⃣ Calculate total
            double totalAmount = 0;
            for (CartItem item : cartItems) {
                totalAmount += item.getTotal();
            }

            // 2️⃣ Insert order
            ps = con.prepareStatement(insertOrderSql);
            ps.setInt(1, buyerId);
            ps.setDouble(2, totalAmount);
            ps.setString(3, "PLACED");
            ps.setString(4, shippingAddress);
            ps.setString(5, paymentMethod);
            ps.setString(6, "SUCCESS");
            ps.executeUpdate();
            ps.close();
            ps = null;

            // 3️⃣ Get order ID
            ps = con.prepareStatement(getOrderIdSql);
            rs = ps.executeQuery();

            if (!rs.next()) {
                con.rollback();
                return -1;
            }

            int orderId = rs.getInt(1);
            rs.close();
            ps.close();
            rs = null;
            ps = null;

            // 4️⃣ Insert items + update stock
            for (CartItem item : cartItems) {

                ps = con.prepareStatement(getProductSql);
                ps.setInt(1, item.getProductId());
                rs = ps.executeQuery();

                if (!rs.next()) {
                    con.rollback();
                    return -1;
                }

                int sellerId = rs.getInt("seller_id");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");

                sellerIds.add(sellerId);

                rs.close();
                ps.close();
                rs = null;
                ps = null;

                if (item.getQuantity() > stock) {
                    con.rollback();
                    return -1;
                }

                double itemTotal = price * item.getQuantity();

                ps = con.prepareStatement(insertItemSql);
                ps.setInt(1, orderId);
                ps.setInt(2, item.getProductId());
                ps.setInt(3, sellerId);
                ps.setInt(4, item.getQuantity());
                ps.setDouble(5, price);
                ps.setDouble(6, itemTotal);
                ps.executeUpdate();
                ps.close();
                ps = null;

                ps = con.prepareStatement(updateStockSql);
                ps.setInt(1, item.getQuantity());
                ps.setInt(2, item.getProductId());
                ps.setInt(3, item.getQuantity());
                ps.executeUpdate();
                ps.close();
                ps = null;
            }

            // ✅ Commit first
            con.commit();

            // 🔔 Buyer notification
            notificationService.notifyUser(
                buyerId,
                "Your order #" + orderId + " has been placed successfully"
            );

            // 🔔 Seller notifications
            for (Integer sellerId : sellerIds) {
                notificationService.notifyUser(
                    sellerId,
                    "New order received (Order ID: " + orderId + ")"
                );
            }

            return orderId;

        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            System.out.println("❌ Place Order Error: " + e.getMessage());
            return -1;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) {}
        }
    }

    // =========================================================
    // ✅ BUYER – VIEW MY ORDERS
    // =========================================================
    public List<OrderDetails> getOrdersByBuyerId(int buyerId) {

        List<OrderDetails> orders = new ArrayList<OrderDetails>();

        String sql =
            "SELECT o.order_id, o.buyer_id, o.status, o.created_at, " +
            "oi.product_id, p.name AS product_name, oi.seller_id, " +
            "oi.quantity, oi.price, oi.item_total " +
            "FROM orders o " +
            "JOIN order_items oi ON o.order_id = oi.order_id " +
            "JOIN products p ON oi.product_id = p.product_id " +
            "WHERE o.buyer_id = ? ORDER BY o.order_id DESC";

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
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return orders;
    }

    // =========================================================
    // ✅ SELLER – VIEW ORDERS
    // =========================================================
    public List<OrderDetails> getOrdersBySellerId(int sellerId) {

        List<OrderDetails> orders = new ArrayList<OrderDetails>();

        String sql =
            "SELECT o.order_id, o.buyer_id, o.status, o.created_at, " +
            "oi.product_id, p.name AS product_name, oi.seller_id, " +
            "oi.quantity, oi.price, oi.item_total " +
            "FROM orders o " +
            "JOIN order_items oi ON o.order_id = oi.order_id " +
            "JOIN products p ON oi.product_id = p.product_id " +
            "WHERE oi.seller_id = ? ORDER BY o.order_id DESC";

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
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return orders;
    }

    // =========================================================
    // ✅ BUYER – CANCEL ORDER
    // =========================================================
    public boolean cancelOrder(int buyerId, int orderId) {

        String checkSql =
            "SELECT status FROM orders WHERE order_id = ? AND buyer_id = ?";

        String updateOrderSql =
            "UPDATE orders SET status = 'CANCELLED' WHERE order_id = ? AND buyer_id = ?";

        String getItemsSql =
            "SELECT product_id, quantity FROM order_items WHERE order_id = ?";

        String restoreStockSql =
            "UPDATE products SET stock = stock + ? WHERE product_id = ?";

        String getSellerSql =
            "SELECT DISTINCT seller_id FROM order_items WHERE order_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Integer> sellerIds = new ArrayList<Integer>();

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // 1️⃣ Validate order
            ps = con.prepareStatement(checkSql);
            ps.setInt(1, orderId);
            ps.setInt(2, buyerId);
            rs = ps.executeQuery();

            if (!rs.next() || !"PLACED".equalsIgnoreCase(rs.getString("status"))) {
                con.rollback();
                return false;
            }
            rs.close();
            ps.close();

            // 2️⃣ Fetch sellerIds FIRST
            ps = con.prepareStatement(getSellerSql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            while (rs.next()) {
                sellerIds.add(rs.getInt("seller_id"));
            }
            rs.close();
            ps.close();

            // 3️⃣ Restore stock
            ps = con.prepareStatement(getItemsSql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            while (rs.next()) {
                PreparedStatement ps2 =
                    con.prepareStatement(restoreStockSql);
                ps2.setInt(1, rs.getInt("quantity"));
                ps2.setInt(2, rs.getInt("product_id"));
                ps2.executeUpdate();
                ps2.close();
            }
            rs.close();
            ps.close();

            // 4️⃣ Cancel order
            ps = con.prepareStatement(updateOrderSql);
            ps.setInt(1, orderId);
            ps.setInt(2, buyerId);
            ps.executeUpdate();
            ps.close();

            // ✅ Commit DB work
            con.commit();

            // 🔔 Buyer notification
            notificationService.notifyUser(
                buyerId,
                "Your order #" + orderId + " has been cancelled successfully"
            );

            // 🔔 Seller notifications
            for (Integer sellerId : sellerIds) {
                notificationService.notifyUser(
                    sellerId,
                    "Order #" + orderId + " has been cancelled by the buyer"
                );
            }

            return true;

        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            System.out.println("Cancel Order Error: " + e.getMessage());
            return false;

        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

}
