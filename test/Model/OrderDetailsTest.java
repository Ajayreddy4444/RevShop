package Model;

import static org.junit.Assert.*;
import org.junit.Test;

public class OrderDetailsTest {

    // Tests OrderDetails getters and setters
    @Test
    public void testOrderDetailsModel() {

        OrderDetails od = new OrderDetails();
        od.setOrderId(1001);
        od.setBuyerId(5);
        od.setProductId(200);
        od.setProductName("Laptop");
        od.setSellerId(9);
        od.setQuantity(1);
        od.setPrice(55000);
        od.setItemTotal(55000);
        od.setStatus("PLACED");
        od.setCreatedAt("2026-01-01");

        assertEquals(1001, od.getOrderId());
        assertEquals(5, od.getBuyerId());
        assertEquals(200, od.getProductId());
        assertEquals("Laptop", od.getProductName());
        assertEquals(9, od.getSellerId());
        assertEquals(1, od.getQuantity());
        assertEquals(55000, od.getPrice(), 0.0);
        assertEquals(55000, od.getItemTotal(), 0.0);
        assertEquals("PLACED", od.getStatus());
        assertEquals("2026-01-01", od.getCreatedAt());
    }
}
