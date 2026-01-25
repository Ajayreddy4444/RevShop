package Model;

public class Product {

    private int productId;
    private int sellerId;
    private String name;
    private String description;
    private String category;
    private double mrp;
    private double price;
    private int stock;

    public Product() {}

    // constructor for adding product
    public Product(int sellerId, String name, String description, String category,
                   double mrp, double price, int stock) {
        this.sellerId = sellerId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.mrp = mrp;
        this.price = price;
        this.stock = stock;
    }

    // getters & setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getMrp() { return mrp; }
    public void setMrp(double mrp) { this.mrp = mrp; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", sellerId=" + sellerId +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", mrp=" + mrp +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
