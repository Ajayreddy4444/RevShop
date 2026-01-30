package Model;

/**
 * Represents an item stored in the buyer's cart
 */
public class CartItem {

    // Product reference
    private int productId;
    private String productName;

    // Pricing and quantity
    private double price;
    private int quantity;
    private double total;

    // Constructor
    public CartItem(int productId, String productName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.total = price * quantity;
    }

    // Get productId
    public int getProductId() {
        return productId;
    }

    // Get product name
    public String getProductName() {
        return productName;
    }

    // Get price
    public double getPrice() {
        return price;
    }

    // Get quantity
    public int getQuantity() {
        return quantity;
    }

    // Get total amount
    public double getTotal() {
        return total;
    }

    // Update quantity and recalculate total
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.total = this.price * this.quantity;
    }
}
