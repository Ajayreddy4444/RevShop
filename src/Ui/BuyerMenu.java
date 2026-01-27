package Ui;

import Model.User;
import Model.Product;
import Model.OrderDetails;
import Service.ProductService;
import Service.CartService;
import Service.OrderService;

import java.util.List;
import java.util.Scanner;

public class BuyerMenu {

    public static void show(User user) {

        ProductService productService = new ProductService();
        CartService cartService = new CartService();   // cart persists
        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== BUYER MENU =====");
            System.out.println("1. View All Products (Add to Cart)");
            System.out.println("2. Search Products by Category");
            System.out.println("3. Search Products by Keyword");
            System.out.println("4. View Cart");
            System.out.println("5. Remove Item from Cart");
            System.out.println("6. Checkout (Buy Now)");
            System.out.println("7. View Orders");
            System.out.println("8. Cancel Order");
            System.out.println("9. Logout");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1: {
                    List<Product> products = productService.viewAllProducts();

                    System.out.println("\n==================== PRODUCT LIST ====================\n");

                    if (products.isEmpty()) {
                        System.out.println("No products available!");
                        break;
                    }

                    System.out.printf("%-5s %-25s %-15s %-10s %-10s%n",
                            "ID", "NAME", "CATEGORY", "PRICE", "STOCK");
                    System.out.println("------------------------------------------------------");

                    for (Product p : products) {
                        System.out.printf("%-5d %-25s %-15s %-10.2f %-10d%n",
                                p.getProductId(),
                                p.getName(),
                                p.getCategory(),
                                p.getPrice(),
                                p.getStock());
                    }

                    System.out.print("\nEnter Product ID to add to cart: ");
                    int productId = sc.nextInt();

                    System.out.print("Enter Quantity: ");
                    int qty = sc.nextInt();
                    sc.nextLine();

                    Product p = productService.getProductById(productId);

                    if (p == null) {
                        System.out.println("❌ Invalid Product ID!");
                        break;
                    }

                    if (qty <= 0) {
                        System.out.println("❌ Quantity must be greater than 0!");
                        break;
                    }

                    if (qty > p.getStock()) {
                        System.out.println("❌ Not enough stock available!");
                        break;
                    }

                    cartService.addToCart(p, qty);
                    break;
                }

                case 2: {
                    List<String> categories = productService.viewAllCategories();

                    if (categories.isEmpty()) {
                        System.out.println("❌ No categories available.");
                        break;
                    }

                    System.out.println("\n===== AVAILABLE CATEGORIES =====");
                    for (int i = 0; i < categories.size(); i++) {
                        System.out.println((i + 1) + ". " + categories.get(i));
                    }

                    System.out.print("Select Category Number: ");
                    int catChoice = sc.nextInt();
                    sc.nextLine();

                    if (catChoice < 1 || catChoice > categories.size()) {
                        System.out.println("❌ Invalid category choice!");
                        break;
                    }

                    String selectedCategory = categories.get(catChoice - 1);
                    List<Product> categoryProducts = productService.viewProductsByCategory(selectedCategory);

                    System.out.println("\nID   NAME                     CATEGORY        PRICE      STOCK");
                    System.out.println("--------------------------------------------------------------");

                    for (Product p : categoryProducts) {
                        System.out.printf("%-4d %-24s %-14s %-10.2f %-5d%n",
                                p.getProductId(),
                                p.getName(),
                                p.getCategory(),
                                p.getPrice(),
                                p.getStock());
                    }
                    break;
                }

                case 3: {
                    System.out.print("Enter Keyword: ");
                    String key = sc.nextLine();

                    List<Product> keyProducts = productService.searchByKeyword(key);

                    System.out.println("\n===== SEARCH RESULTS =====");

                    if (keyProducts.isEmpty()) {
                        System.out.println("No products found with this keyword!");
                    } else {
                        System.out.printf("%-5s %-25s %-15s %-10s %-10s%n",
                                "ID", "NAME", "CATEGORY", "PRICE", "STOCK");
                        System.out.println("------------------------------------------------------");

                        for (Product p : keyProducts) {
                            System.out.printf("%-5d %-25s %-15s %-10.2f %-10d%n",
                                    p.getProductId(),
                                    p.getName(),
                                    p.getCategory(),
                                    p.getPrice(),
                                    p.getStock());
                        }
                    }
                    break;
                }

                case 4:
                    cartService.viewCart();
                    break;

                case 5:
                    System.out.print("Enter Product ID to remove: ");
                    int removeId = sc.nextInt();
                    sc.nextLine();
                    cartService.removeFromCart(removeId);
                    break;

                case 6:
                    if (cartService.isEmpty()) {
                        System.out.println("🛒 Cart is empty! Add products first.");
                        break;
                    }

                    cartService.viewCart();

                    // ✅ Step 1: Shipping Address
                    System.out.print("Enter Shipping Address: ");
                    String address = sc.nextLine();

                    if (address == null || address.trim().isEmpty()) {
                        System.out.println("❌ Shipping address cannot be empty!");
                        break;
                    }

                    // ✅ Step 2: Payment Simulation
                    System.out.println("\n===== PAYMENT OPTIONS =====");
                    System.out.println("1. Cash on Delivery (COD)");
                    System.out.println("2. UPI");
                    System.out.println("3. Card");
                    System.out.print("Select Payment Method: ");

                    int payChoice = sc.nextInt();
                    sc.nextLine(); // consume newline

                    String paymentMethod = "";

                    if (payChoice == 1) {
                        paymentMethod = "COD";
                        System.out.println("✅ COD Selected. Payment will be collected at delivery.");
                    }
                    else if (payChoice == 2) {
                        paymentMethod = "UPI";
                        System.out.print("Enter UPI ID: ");
                        String upiId = sc.nextLine();

                        if (upiId == null || upiId.trim().isEmpty()) {
                            System.out.println("❌ Invalid UPI ID! Payment Failed.");
                            break;
                        }

                        System.out.println("✅ Payment Successful via UPI!");
                    }
                    else if (payChoice == 3) {
                        paymentMethod = "CARD";
                        System.out.print("Enter Card Number (last 4 digits): ");
                        String cardLast4 = sc.nextLine();

                        if (cardLast4 == null || cardLast4.trim().length() != 4) {
                            System.out.println("❌ Invalid Card Details! Payment Failed.");
                            break;
                        }

                        System.out.println("✅ Payment Successful via Card!");
                    }
                    else {
                        System.out.println("❌ Invalid payment choice!");
                        break;
                    }

                    // ✅ Step 3: Confirm Checkout
                    System.out.print("\nConfirm Checkout? (yes/no): ");
                    String confirmCheckout = sc.nextLine();

                    if (confirmCheckout.equalsIgnoreCase("yes")) {

                        OrderService orderService = new OrderService();

                        int orderId = orderService.checkout(
                                user.getUserId(),
                                cartService.getCartItems(),
                                address,
                                paymentMethod
                        );

                        if (orderId != -1) {
                            System.out.println("✅ Order Placed Successfully! Order ID = " + orderId);
                            cartService.clearCart();
                        } else {
                            System.out.println("❌ Order Failed!");
                        }

                    } else {
                        System.out.println("❌ Checkout cancelled.");
                    }
                    break;



                case 7:
                    OrderService orderService = new OrderService();
                    List<OrderDetails> myOrders = orderService.viewBuyerOrders(user.getUserId());

                    System.out.println("\n========== MY ORDERS ==========");

                    if (myOrders.isEmpty()) {
                        System.out.println("❌ No orders found!");
                    } else {
                        System.out.printf("%-8s %-8s %-20s %-8s %-10s %-10s %-15s%n",
                                "ORDERID", "PID", "PRODUCT", "QTY", "PRICE", "TOTAL", "STATUS");
                        System.out.println("--------------------------------------------------------------------------");

                        for (OrderDetails o : myOrders) {
                            System.out.printf("%-8d %-8d %-20s %-8d %-10.2f %-10.2f %-15s%n",
                                    o.getOrderId(),
                                    o.getProductId(),
                                    o.getProductName(),
                                    o.getQuantity(),
                                    o.getPrice(),
                                    o.getItemTotal(),
                                    o.getStatus());
                        }
                    }
                    break;

                case 8:
                    System.out.print("Enter Order ID to cancel: ");
                    int cancelId = sc.nextInt();
                    sc.nextLine();

                    OrderService cancelService = new OrderService();
                    boolean cancelled = cancelService.cancelOrder(user.getUserId(), cancelId);

                    if (cancelled) {
                        System.out.println("✅ Cancelled Successfully!");
                    } else {
                        System.out.println("❌ Cancel Failed!");
                    }
                    break;

                case 9:
                    System.out.println("✅ Logged out successfully!");
                    return;

                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
}
