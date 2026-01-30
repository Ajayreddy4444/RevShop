package Model;

/**
 * Represents a product listed by a seller
 */
public class Product {

    // Product primary key
    private int productId;

    // Seller who owns the product
    private int sellerId;

    // Product details
    private String name;
    private String description;
    private String category;

    // Pricing details
    private double mrp;
    private double price;

    // Available stock
    private int stock;

    // Default constructor
    public Product() {
    }

    // Constructor for adding a new product
    public Product(int sellerId, String name, String description,
                   String category, double mrp, double price, int stock) {
        this.sellerId = sellerId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.mrp = mrp;
        this.price = price;
        this.stock = stock;
    }

    // Get productId
    public int getProductId() {
        return productId;
    }

    // Set productId
    public void setProductId(int productId) {
        this.productId = productId;
    }

    // Get sellerId
    public int getSellerId() {
        return sellerId;
    }

    // Set sellerId
    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    // Get name
    public String getName() {
        return name;
    }

    // Set name
    public void setName(String name) {
        this.name = name;
    }

    // Get description
    public String getDescription() {
        return description;
    }

    // Set description
    public void setDescription(String description) {
        this.description = description;
    }

    // Get category
    public String getCategory() {
        return category;
    }

    // Set category
    public void setCategory(String category) {
        this.category = category;
    }

    // Get MRP
    public double getMrp() {
        return mrp;
    }

    // Set MRP
    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    // Get selling price
    public double getPrice() {
        return price;
    }

    // Set selling price
    public void setPrice(double price) {
        this.price = price;
    }

    // Get stock
    public int getStock() {
        return stock;
    }

    // Set stock
    public void setStock(int stock) {
        this.stock = stock;
    }
}
