package Service;

import Dao.ProductDAO;
import Exception.ProductException;
import Exception.ValidationException;
import Model.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductService {

    private ProductDAO productDAO = new ProductDAO();

    // Add product
    public boolean addProduct(Product product) {

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

    // View all products
    public List<Product> viewAllProducts() {
        return productDAO.getAllProducts();
    }
    
 // Fetches all available categories
    public List<String> viewAllCategories() {

        List<String> categories = productDAO.getAllCategories();

        if (categories == null || categories.isEmpty()) {
            throw new ProductException("No product categories available");
        }

        return categories;
    }


    // View by category
    public List<Product> viewProductsByCategory(String category) {

        if (category == null || category.trim().isEmpty()) {
            throw new ProductException("Category cannot be empty");
        }

        return productDAO.getProductsByCategory(category);
    }

    // Search
    public List<Product> searchByKeyword(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ProductException("Search keyword cannot be empty");
        }

        return productDAO.searchProductsByKeyword(keyword);
    }

    // Get by ID
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
    
    //view products by sellerId
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

    // Update product
    public boolean updateProduct(Product product) {

        if (product.getProductId() <= 0) {
            throw new ProductException("Invalid product ID");
        }

        return productDAO.updateProduct(product);
    }

    // Delete product
    public void deleteProduct(int productId, int sellerId) {

        if (productId <= 0) {
            throw new ProductException("Invalid product ID");
        }

        try {
            boolean deleted = productDAO.deleteProduct(productId, sellerId);

            if (!deleted) {
                throw new ProductException("Product not found or not owned by seller");
            }

        } catch (RuntimeException e) {

            // 🔥 FK constraint → product already ordered
            if ("FK_CONSTRAINT".equals(e.getMessage())) {
                throw new ProductException(
                    "Cannot delete product. It has already been ordered."
                );
            }

            throw new ProductException("Failed to delete product");
        }
    }

    
    public void deactivateProduct(int productId, int sellerId) {

        boolean success = productDAO.deactivateProduct(productId, sellerId);

        if (!success) {
            throw new ProductException("Failed to deactivate product");
        }
    }




}
