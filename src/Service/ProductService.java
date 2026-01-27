package Service;

import Dao.ProductDAO;
import Model.Product;

import java.util.List;

public class ProductService {

    private ProductDAO productDAO = new ProductDAO();

    public boolean addProduct(Product product) {

        // basic validations
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            System.out.println("❌ Product name cannot be empty!");
            return false;
        }

        if (product.getPrice() <= 0 || product.getMrp() <= 0) {
            System.out.println("❌ Price and MRP must be greater than 0!");
            return false;
        }

        if (product.getPrice() > product.getMrp()) {
            System.out.println("❌ Price cannot be greater than MRP!");
            return false;
        }

        if (product.getStock() < 0) {
            System.out.println("❌ Stock cannot be negative!");
            return false;
        }

        return productDAO.addProduct(product);
    }

    public List<Product> viewAllProducts() {
        return productDAO.getAllProducts();
    }

    public List<Product> viewProductsByCategory(String category) {
        return productDAO.getProductsByCategory(category);
    }

    public List<Product> searchByKeyword(String keyword) {
        return productDAO.searchProductsByKeyword(keyword);
    }

    public List<String> viewAllCategories() {
        return productDAO.getAllCategories();
    }

    public List<Product> viewProductsBySeller(int sellerId) {
        return productDAO.getProductsBySellerId(sellerId);
    }

    public Product getProductById(int productId) {
        return productDAO.getProductById(productId);
    }
    
    public boolean updateProduct(Product product) {

        if (product.getProductId() <= 0) {
            System.out.println("❌ Invalid Product ID!");
            return false;
        }

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            System.out.println("❌ Product name cannot be empty!");
            return false;
        }

        if (product.getPrice() <= 0 || product.getMrp() <= 0) {
            System.out.println("❌ Price and MRP must be greater than 0!");
            return false;
        }

        if (product.getPrice() > product.getMrp()) {
            System.out.println("❌ Price cannot be greater than MRP!");
            return false;
        }

        if (product.getStock() < 0) {
            System.out.println("❌ Stock cannot be negative!");
            return false;
        }

        return productDAO.updateProduct(product);
    }

    public boolean deleteProduct(int productId, int sellerId) {

        if (productId <= 0) {
            System.out.println("❌ Invalid Product ID!");
            return false;
        }

        return productDAO.deleteProduct(productId, sellerId);
    }




}
