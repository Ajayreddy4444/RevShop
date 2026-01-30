package Service;

import Dao.OrderDAO;
import Exception.OrderException;
import Model.CartItem;
import Model.OrderDetails;

import java.util.List;

public class OrderService {

    private OrderDAO orderDAO = new OrderDAO();

    /**
     * Places an order for buyer using cart items
     */
    public int checkout(int buyerId, List<CartItem> cartItems,
                        String address, String paymentMethod)
            throws OrderException {

        // 1️⃣ Validate cart
        if (cartItems == null || cartItems.isEmpty()) {
            throw new OrderException("Cart is empty. Add products before checkout.");
        }

        // 2️⃣ Validate address
        if (address == null || address.trim().isEmpty()) {
            throw new OrderException("Shipping address cannot be empty.");
        }

        // 3️⃣ Validate payment
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new OrderException("Invalid payment method.");
        }

        // 4️⃣ Place order
        int orderId = orderDAO.placeOrder(
                buyerId,
                cartItems,
                address,
                paymentMethod
        );

        // 5️⃣ Handle failure
        if (orderId == -1) {
            throw new OrderException("Order placement failed. Please try again.");
        }

        return orderId;
    }

    /**
     * Fetches all orders placed by a buyer
     */
    public List<OrderDetails> viewBuyerOrders(int buyerId) {
        return orderDAO.getOrdersByBuyerId(buyerId);
    }

    /**
     * Fetches all orders for seller's products
     */
    public List<OrderDetails> viewSellerOrders(int sellerId) {
        return orderDAO.getOrdersBySellerId(sellerId);
    }

    /**
     * Cancels an order placed by buyer
     */
    public boolean cancelOrder(int buyerId, int orderId)
            throws OrderException {

        boolean cancelled = orderDAO.cancelOrder(buyerId, orderId);

        if (!cancelled) {
            throw new OrderException(
                "Order cannot be cancelled (Invalid ID or already processed)."
            );
        }

        return true;
    }
}
