package Model;

/**
 * Represents order and order item details
 */
public class OrderDetails {

    // Order identifiers
    private int orderId;
    private int buyerId;
    private int sellerId;
    private int productId;

    // Product details
    private String productName;

    // Order item details
    private int quantity;
    private double price;
    private double itemTotal;

    // Order status and time
    private String status;
    private String createdAt;

    // Get orderId
    public int getOrderId() {
        return orderId;
    }

    // Set orderId
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    // Get buyerId
    public int getBuyerId() {
        return buyerId;
    }

    // Set buyerId
    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    // Get sellerId
    public int getSellerId() {
        return sellerId;
    }

    // Set sellerId
    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    // Get productId
    public int getProductId() {
        return productId;
    }

    // Set productId
    public void setProductId(int productId) {
        this.productId = productId;
    }

    // Get product name
    public String getProductName() {
        return productName;
    }

    // Set product name
    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Get quantity
    public int getQuantity() {
        return quantity;
    }

    // Set quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Get price
    public double getPrice() {
        return price;
    }

    // Set price
    public void setPrice(double price) {
        this.price = price;
    }

    // Get item total
    public double getItemTotal() {
        return itemTotal;
    }

    // Set item total
    public void setItemTotal(double itemTotal) {
        this.itemTotal = itemTotal;
    }

    // Get status
    public String getStatus() {
        return status;
    }

    // Set status
    public void setStatus(String status) {
        this.status = status;
    }

    // Get created time
    public String getCreatedAt() {
        return createdAt;
    }

    // Set created time
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
