package Service;

import Dao.OrderDAO;
import Exception.OrderException;
import Model.CartItem;
import Model.OrderDetails;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Handles order placement, retrieval, and cancellation logic.
 */
public class OrderService {

    private static final Logger logger =
            Logger.getLogger(OrderService.class);

    private OrderDAO orderDAO;

    /**
     * Default constructor.
     */
    public OrderService() {
        this.orderDAO = new OrderDAO();
    }

    /**
     * Constructor for Mockito testing.
     *
     * @param orderDAO mocked OrderDAO
     */
    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    /**
     * Places an order for a buyer.
     *
     * @param buyerId buyer ID
     * @param cartItems cart items
     * @param address shipping address
     * @param paymentMethod payment method
     * @return generated order ID
     */
    public int checkout(int buyerId,
                        List<CartItem> cartItems,
                        String address,
                        String paymentMethod) {

        if (cartItems == null || cartItems.isEmpty()) {
            throw new OrderException("Cart is empty. Add products before checkout.");
        }

        if (address == null || address.trim().isEmpty()) {
            throw new OrderException("Shipping address cannot be empty.");
        }

        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new OrderException("Invalid payment method.");
        }

        int orderId = orderDAO.placeOrder(
                buyerId, cartItems, address, paymentMethod);

        if (orderId == -1) {
            throw new OrderException("Order placement failed.");
        }

        return orderId;
    }

    /**
     * Retrieves buyer orders.
     *
     * @param buyerId buyer ID
     * @return list of orders
     */
    public List<OrderDetails> viewBuyerOrders(int buyerId) {
        return orderDAO.getOrdersByBuyerId(buyerId);
    }

    /**
     * Retrieves seller orders.
     *
     * @param sellerId seller ID
     * @return list of orders
     */
    public List<OrderDetails> viewSellerOrders(int sellerId) {
        return orderDAO.getOrdersBySellerId(sellerId);
    }

    /**
     * Cancels a buyer order.
     *
     * @param buyerId buyer ID
     * @param orderId order ID
     * @return {@code true} if cancelled
     */
    public boolean cancelOrder(int buyerId, int orderId) {

        boolean cancelled = orderDAO.cancelOrder(buyerId, orderId);

        if (!cancelled) {
            throw new OrderException(
                "Order cannot be cancelled (Invalid ID or already processed).");
        }

        return true;
    }
}
