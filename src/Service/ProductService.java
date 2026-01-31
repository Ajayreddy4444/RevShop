package Service;

import Dao.ProductDAO;
import Exception.ProductException;
import Exception.ValidationException;
import Model.Product;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Provides product-related business operations.
 *
 * <p>
 * This service handles validation and coordination between
 * UI layers and {@link ProductDAO}.
 * </p>
 */
public class ProductService {

    private static final Logger logger =
            Logger.getLogger(ProductService.class);

    private ProductDAO productDAO;

    /**
     * Default constructor for normal application execution.
     */
    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    /**
     * Constructor used for Mockito-based unit testing.
     *
     * @param productDAO mocked ProductDAO instance
     */
    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * Adds a new product to the system.
     *
     * <p>
     * Performs validation on product attributes before
     * delegating persistence to DAO.
     * </p>
     *
     * @param product the product to add
     * @return {@code true} if product is added successfully
     */
    public boolean addProduct(Product product) {

        logger.info("Attempting to add product");

        if (product == null) {
            throw new ProductException("Product cannot be null");
        }

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new ProductException("Product name is required");
        }

        if (product.getPrice() <= 0 || product.getMrp() <= 0) {
            throw new ProductException("Price and MRP must be greater than zero");
        }

        if (product.getPrice() > product.getMrp()) {
            throw new ProductException("Price cannot be greater than MRP");
        }

        if (product.getStock() < 0) {
            throw new ProductException("Stock cannot be negative");
        }

        return productDAO.addProduct(product);
    }

    /**
     * Retrieves all products.
     *
     * @return list of products
     */
    public List<Product> viewAllProducts() {
        return productDAO.getAllProducts();
    }

    /**
     * Retrieves all available product categories.
     *
     * @return list of category names
     */
    public List<String> viewAllCategories() {

        List<String> categories = productDAO.getAllCategories();

        if (categories == null || categories.isEmpty()) {
            throw new ProductException("No product categories available");
        }

        return categories;
    }

    /**
     * Retrieves products by category.
     *
     * @param category category name
     * @return list of products
     */
    public List<Product> viewProductsByCategory(String category) {

        if (category == null || category.trim().isEmpty()) {
            throw new ProductException("Category cannot be empty");
        }

        return productDAO.getProductsByCategory(category);
    }

    /**
     * Searches products using a keyword.
     *
     * @param keyword search term
     * @return list of matched products
     */
    public List<Product> searchByKeyword(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ProductException("Search keyword cannot be empty");
        }

        return productDAO.searchProductsByKeyword(keyword);
    }

    /**
     * Retrieves a product by ID.
     *
     * @param productId product ID
     * @return product object
     */
    public Product getProductById(int productId) {

        if (productId <= 0) {
            throw new ProductException("Invalid product ID");
        }

        Product product = productDAO.getProductById(productId);

        if (product == null) {
            throw new ProductException("Product not found");
        }

        return product;
    }

    /**
     * Retrieves products belonging to a seller.
     *
     * @param sellerId seller ID
     * @return list of products
     */
    public List<Product> viewProductsBySeller(int sellerId) {

        if (sellerId <= 0) {
            throw new ValidationException("Invalid seller ID");
        }

        List<Product> products = productDAO.getProductsBySellerId(sellerId);

        if (products == null || products.isEmpty()) {
            throw new ProductException("No products found for this seller");
        }

        return products;
    }

    /**
     * Updates an existing product.
     *
     * @param product updated product
     * @return {@code true} if update succeeds
     */
    public boolean updateProduct(Product product) {

        if (product.getProductId() <= 0) {
            throw new ProductException("Invalid product ID");
        }

        return productDAO.updateProduct(product);
    }

    /**
     * Deletes a product owned by a seller.
     *
     * @param productId product ID
     * @param sellerId seller ID
     */
    public void deleteProduct(int productId, int sellerId) {

        if (productId <= 0) {
            throw new ProductException("Invalid product ID");
        }

        boolean deleted = productDAO.deleteProduct(productId, sellerId);

        if (!deleted) {
            throw new ProductException("Product not found or not owned by seller");
        }
    }

    /**
     * Deactivates a product.
     *
     * @param productId product ID
     * @param sellerId seller ID
     */
    public void deactivateProduct(int productId, int sellerId) {

        boolean success = productDAO.deactivateProduct(productId, sellerId);

        if (!success) {
            throw new ProductException("Failed to deactivate product");
        }
    }
}
