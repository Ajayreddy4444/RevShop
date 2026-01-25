package Service;

import Dao.OrderDAO;
import Model.CartItem;
import Model.OrderDetails;

import java.util.List;

public class OrderService {

    private OrderDAO orderDAO = new OrderDAO();

    public int checkout(int buyerId, List<CartItem> cartItems) {

        if (cartItems == null || cartItems.isEmpty()) {
            System.out.println("🛒 Cart is empty!");
            return -1;
        }

        return orderDAO.placeOrder(buyerId, cartItems);
    }

    public List<OrderDetails> viewBuyerOrders(int buyerId) {
        return orderDAO.getOrdersByBuyerId(buyerId);
    }

    public List<OrderDetails> viewSellerOrders(int sellerId) {
        return orderDAO.getOrdersBySellerId(sellerId);
    }
}
