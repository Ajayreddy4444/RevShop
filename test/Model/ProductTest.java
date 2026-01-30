package Model;

import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {

    @Test
    public void testProductModel() {

        Product p = new Product();
        p.setProductId(10);
        p.setName("Laptop");
        p.setCategory("Electronics");
        p.setPrice(50000);
        p.setStock(5);

        assertEquals(10, p.getProductId());
        assertEquals("Laptop", p.getName());
        assertEquals("Electronics", p.getCategory());
        assertEquals(50000, p.getPrice(), 0.0);
        assertEquals(5, p.getStock());
    }
}
