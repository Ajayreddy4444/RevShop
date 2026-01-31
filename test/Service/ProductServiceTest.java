package Service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Dao.ProductDAO;
import Exception.ProductException;
import Model.Product;

public class ProductServiceTest {

    private ProductService productService;
    private ProductDAO productDAOMock;

    @Before
    public void setUp() {
        productDAOMock = mock(ProductDAO.class);
        productService = new ProductService(productDAOMock);
    }

    @Test
    public void testViewAllProducts() {
        when(productDAOMock.getAllProducts())
                .thenReturn(Arrays.asList(new Product()));

        List<Product> products = productService.viewAllProducts();

        assertEquals(1, products.size());
        verify(productDAOMock).getAllProducts();
    }

    @Test(expected = ProductException.class)
    public void testGetProductByInvalidId() {
        productService.getProductById(0);
    }
}
