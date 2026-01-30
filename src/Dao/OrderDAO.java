package Dao;

import Model.CartItem;
import Model.OrderDetails;
import Service.NotificationService;
import Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private static final int LOW_STOCK_THRESHOLD = 5;
    private NotificationService notificationService = new NotificationService();

    // Places an order with items, payment, stock update, and notifications
    public int placeOrder(int buyerId, List<CartItem> cartItems,
                          String shippingAddress, String paymentMethod) {

        String insertOrderSql =
            "INSERT INTO orders (buyer_id, total_amount, status, shipping_address, payment_method, payment_status) " +
            "VALUES (?, ?, 'PLACED', ?, ?, 'SUCCESS')";

        String getOrderIdSql =
            "SELECT orders_seq.CURRVAL FROM dual";

        String getProductSql =
            "SELECT seller_id, price, stock FROM products WHERE product_id = ?";

        String insertItemSql =
            "INSERT INTO order_items (order_id, product_id, seller_id, quantity, price, item_total) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

        String updateStockSql =
            "UPDATE products SET stock = stock - ? WHERE product_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Integer> sellerIds = new ArrayList<Integer>();
        List<String> lowStockMessages = new ArrayList<String>();

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // Calculate order total
            double totalAmount = 0;
            for (CartItem item : cartItems) {
                totalAmount += item.getTotal();
            }

            // Insert order
            ps = con.prepareStatement(insertOrderSql);
            ps.setInt(1, buyerId);
            ps.setDouble(2, totalAmount);
            ps.setString(3, shippingAddress);
            ps.setString(4, paymentMethod);
            ps.executeUpdate();
            ps.close();

            // Get generated order ID
            ps = con.prepareStatement(getOrderIdSql);
            rs = ps.executeQuery();
            if (!rs.next()) {
                con.rollback();
                return -1;
            }
            int orderId = rs.getInt(1);
            rs.close();
            ps.close();

            // Process each cart item
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

                rs.close();
                ps.close();

                if (item.getQuantity() > stock) {
                    con.rollback();
                    return -1;
                }

                double itemTotal = price * item.getQuantity();
                int remainingStock = stock - item.getQuantity();

                ps = con.prepareStatement(insertItemSql);
                ps.setInt(1, orderId);
                ps.setInt(2, item.getProductId());
                ps.setInt(3, sellerId);
                ps.setInt(4, item.getQuantity());
                ps.setDouble(5, price);
                ps.setDouble(6, itemTotal);
                ps.executeUpdate();
                ps.close();

                ps = con.prepareStatement(updateStockSql);
                ps.setInt(1, item.getQuantity());
                ps.setInt(2, item.getProductId());
                ps.executeUpdate();
                ps.close();

                sellerIds.add(sellerId);

                if (remainingStock <= LOW_STOCK_THRESHOLD) {
                    lowStockMessages.add(
                        "⚠️ Low stock alert! Product ID " + item.getProductId() +
                        " has only " + remainingStock + " items left"
                    );
                }
            }

            con.commit();

            // Buyer notification
            notificationService.notifyUser(
                buyerId,
                "Your order #" + orderId + " has been placed successfully"
            );

            // Seller notifications
            for (Integer sellerId : sellerIds) {
                notificationService.notifyUser(
                    sellerId,
                    "New order received (Order ID: " + orderId + ")"
                );
            }

            // Low stock notifications
            for (int i = 0; i < lowStockMessages.size(); i++) {
                notificationService.notifyUser(sellerIds.get(i), lowStockMessages.get(i));
            }

            return orderId;

        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            System.out.println("Place Order Error: " + e.getMessage());
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

    // Fetches all orders placed by a buyer
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
            System.out.println("Buyer Orders Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return orders;
    }

    // Fetches all orders for a seller’s products
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
            System.out.println("Seller Orders Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return orders;
    }

    // Checks whether a buyer has purchased a product
    public boolean hasBuyerPurchasedProduct(int buyerId, int productId) {

        String sql =
            "SELECT COUNT(*) FROM order_items oi " +
            "JOIN orders o ON oi.order_id = o.order_id " +
            "WHERE o.buyer_id = ? AND oi.product_id = ?";

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
            System.out.println("Purchase Check Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return false;
    }
    
 // Cancels an order placed by buyer and restores stock
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

            // 1️⃣ Validate order status
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

            // 2️⃣ Restore stock
            ps = con.prepareStatement(getItemsSql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();

            while (rs.next()) {
                PreparedStatement ps2 = con.prepareStatement(restoreStockSql);
                ps2.setInt(1, rs.getInt("quantity"));
                ps2.setInt(2, rs.getInt("product_id"));
                ps2.executeUpdate();
                ps2.close();
            }
            rs.close();
            ps.close();

            // 3️⃣ Update order status
            ps = con.prepareStatement(updateOrderSql);
            ps.setInt(1, orderId);
            ps.setInt(2, buyerId);
            ps.executeUpdate();
            ps.close();

            con.commit();

            // 4️⃣ Notifications
            notificationService.notifyUser(
                buyerId,
                "Your order #" + orderId + " has been cancelled successfully"
            );

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
