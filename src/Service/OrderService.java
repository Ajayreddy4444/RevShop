package Service;

import Dao.OrderDAO;
import Model.CartItem;
import Model.OrderDetails;

import java.util.List;

public class OrderService {

    private OrderDAO orderDAO = new OrderDAO();

    // ✅ Checkout with Shipping + Payment
    public int checkout(int buyerId, List<CartItem> cartItems, String shippingAddress, String paymentMethod) {

        if (cartItems == null || cartItems.isEmpty()) {
            System.out.println("🛒 Cart is empty!");
            return -1;
        }

        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            System.out.println("❌ Shipping Address cannot be empty!");
            return -1;
        }

        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            System.out.println("❌ Payment Method cannot be empty!");
            return -1;
        }

        // ✅ Call updated DAO method
        return orderDAO.placeOrder(buyerId, cartItems, shippingAddress, paymentMethod);
    }

    public List<OrderDetails> viewBuyerOrders(int buyerId) {
        return orderDAO.getOrdersByBuyerId(buyerId);
    }

    public List<OrderDetails> viewSellerOrders(int sellerId) {
        return orderDAO.getOrdersBySellerId(sellerId);
    }

    // ✅ Cancel order (feature)
    public boolean cancelOrder(int buyerId, int orderId) {
        if (orderId <= 0) {
            System.out.println("❌ Invalid Order ID!");
            return false;
        }
        return orderDAO.cancelOrder(buyerId, orderId);
    }
}
