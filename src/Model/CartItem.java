package Model;

public class CartItem {

    private int productId;
    private String productName;
    private double price;
    private int quantity;
    private double total;

    public CartItem(int productId, String productName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.total = price * quantity;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.total = this.price * this.quantity;
    }
}
