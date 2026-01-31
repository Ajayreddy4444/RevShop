package Service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Dao.OrderDAO;
import Exception.OrderException;
import Model.CartItem;

public class OrderServiceTest {

    private OrderService orderService;
    private OrderDAO orderDAOMock;

    @Before
    public void setUp() {
        orderDAOMock = mock(OrderDAO.class);
        orderService = new OrderService(orderDAOMock);
    }

    @Test
    public void testCheckoutSuccess() {
        List<CartItem> cart = new ArrayList<CartItem>();
        cart.add(new CartItem(1, "Phone", 10000, 1));

        when(orderDAOMock.placeOrder(anyInt(), anyList(), anyString(), anyString()))
                .thenReturn(101);

        int orderId = orderService.checkout(1, cart, "Hyderabad", "COD");

        assertEquals(101, orderId);
    }

    @Test(expected = OrderException.class)
    public void testCheckoutEmptyCart() {
        orderService.checkout(1, new ArrayList<CartItem>(), "Hyd", "COD");
    }
}
