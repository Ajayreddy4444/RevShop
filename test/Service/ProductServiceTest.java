package Service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Exception.ProductException;
import Exception.ValidationException;
import Model.Product;

public class ProductServiceTest {

    private ProductService productService;

    @Before
    public void setUp() {
        productService = new ProductService();
    }

    // ================= ADD PRODUCT TESTS =================

    // ❌ Null product
    @Test(expected = ProductException.class)
    public void testAddProduct_NullProduct() {
        productService.addProduct(null);
    }

    // ❌ Empty product name
    @Test(expected = ProductException.class)
    public void testAddProduct_EmptyName() {
        Product p = new Product();
        p.setName("");
        p.setPrice(100);
        p.setMrp(120);
        p.setStock(10);

        productService.addProduct(p);
    }

    // ❌ Price greater than MRP
    @Test(expected = ProductException.class)
    public void testAddProduct_PriceGreaterThanMrp() {
        Product p = new Product();
        p.setName("Mobile");
        p.setPrice(1500);
        p.setMrp(1000);
        p.setStock(5);

        productService.addProduct(p);
    }

    // ❌ Negative stock
    @Test(expected = ProductException.class)
    public void testAddProduct_NegativeStock() {
        Product p = new Product();
        p.setName("Laptop");
        p.setPrice(50000);
        p.setMrp(55000);
        p.setStock(-1);

        productService.addProduct(p);
    }

    // ================= VIEW CATEGORY TESTS =================

    // ❌ Empty category
    @Test(expected = ProductException.class)
    public void testViewProductsByCategory_EmptyCategory() {
        productService.viewProductsByCategory("");
    }

    // ================= SEARCH TESTS =================

    // ❌ Empty keyword
    @Test(expected = ProductException.class)
    public void testSearchByKeyword_EmptyKeyword() {
        productService.searchByKeyword("");
    }

    // ================= GET PRODUCT BY ID =================

    // ❌ Invalid product ID
    @Test(expected = ProductException.class)
    public void testGetProductById_InvalidId() {
        productService.getProductById(0);
    }


    

    // ================= VIEW PRODUCTS BY SELLER =================

    // ❌ Invalid seller ID
    @Test(expected = ValidationException.class)
    public void testViewProductsBySeller_InvalidSellerId() {
        productService.viewProductsBySeller(0);
    }
}
