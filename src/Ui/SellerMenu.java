package Ui;

import Exception.AuthException;
import Exception.InvalidCredentialsException;
import Exception.ProductException;
import Exception.ValidationException;
import Model.User;
import Model.Product;
import Model.OrderDetails;
import Service.AuthService;
import Service.ProductService;
import Service.OrderService;
import Service.NotificationService;
import Model.Notification;
import java.util.List;
import Service.ReviewService;
import Model.Review;
import java.util.Scanner;

public class SellerMenu {

    public static void show(User user) {

        Scanner sc = new Scanner(System.in);
        ProductService productService = new ProductService();

        while (true) {
            System.out.println("\n===== SELLER MENU =====");
            
            System.out.println("Welcome, " + user.getName());
            System.out.println("1. View My Products");
            System.out.println("2. Add Product");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. View Orders");
            System.out.println("6. View Product Reviews");
            System.out.println("7. View Notifications");
            System.out.println("8. Change Password");
            System.out.println("9. Logout");

            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
            
            case 1:
                try {
                    List<Product> myProducts =
                            productService.viewProductsBySeller(user.getUserId());

                    System.out.println("\n========== MY PRODUCTS ==========");
                    System.out.printf("%-5s %-20s %-15s %-10s %-10s %-10s%n",
                            "ID", "NAME", "CATEGORY", "MRP", "PRICE", "STOCK");
                    System.out.println("-------------------------------------------------------------");

                    for (Product p : myProducts) {
                        System.out.printf("%-5d %-20s %-15s %-10.2f %-10.2f %-10d%n",
                                p.getProductId(),
                                p.getName(),
                                p.getCategory(),
                                p.getMrp(),
                                p.getPrice(),
                                p.getStock());
                    }

                } catch (ProductException e) {
                    System.out.println("❌ " + e.getMessage());
                }
                break;


                case 2:
                    System.out.print("Enter Product Name: ");
                    String pname = sc.nextLine();

                    System.out.print("Enter Description: ");
                    String desc = sc.nextLine();

                    System.out.print("Enter Category: ");
                    String category = sc.nextLine();

                    System.out.print("Enter MRP: ");
                    double mrp = sc.nextDouble();

                    System.out.print("Enter Selling Price: ");
                    double price = sc.nextDouble();

                    System.out.print("Enter Stock Quantity: ");
                    int stock = sc.nextInt();
                    sc.nextLine(); // consume newline

                    Product product = new Product(user.getUserId(), pname, desc, category, mrp, price, stock);

                    if (productService.addProduct(product)) {
                        System.out.println("✅ Product Added Successfully!");
                    } else {
                        System.out.println("❌ Failed to Add Product!");
                    }
                    break;
                    
                case 3:
                    System.out.print("Enter Product ID to Update: ");
                    int updateId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter New Product Name: ");
                    String newName = sc.nextLine();

                    System.out.print("Enter New Description: ");
                    String newDesc = sc.nextLine();

                    System.out.print("Enter New Category: ");
                    String newCategory = sc.nextLine();

                    System.out.print("Enter New MRP: ");
                    double newMrp = sc.nextDouble();

                    System.out.print("Enter New Selling Price: ");
                    double newPrice = sc.nextDouble();

                    System.out.print("Enter New Stock Quantity: ");
                    int newStock = sc.nextInt();
                    sc.nextLine();

                    Product updatedProduct = new Product(user.getUserId(), newName, newDesc, newCategory, newMrp, newPrice, newStock);
                    updatedProduct.setProductId(updateId);

                    if (productService.updateProduct(updatedProduct)) {
                        System.out.println("✅ Product Updated Successfully!");
                    } else {
                        System.out.println("❌ Update Failed! (Check Product ID / Ownership)");
                    }
                    break;

                case 4:
                    System.out.print("Enter Product ID to Delete: ");
                    int pid = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Are you sure? (yes/no): ");
                    String confirm = sc.nextLine();

                    if (!confirm.equalsIgnoreCase("yes")) {
                        System.out.println("❌ Delete cancelled.");
                        break;
                    }

                    try {
                        productService.deleteProduct(pid, user.getUserId());
                        System.out.println("✅ Product deleted successfully!");

                    } catch (ProductException pe) {

                        System.out.println("❌ " + pe.getMessage());

                        // 🔥 ASK FOR DEACTIVATION
                        if (pe.getMessage().contains("already been ordered")) {

                            System.out.print("Do you want to deactivate the product instead? (yes/no): ");
                            String option = sc.nextLine();

                            if (option.equalsIgnoreCase("yes")) {
                                try {
                                    productService.deactivateProduct(pid, user.getUserId());
                                    System.out.println("✅ Product deactivated successfully!");
                                } catch (ProductException ex) {
                                    System.out.println("❌ " + ex.getMessage());
                                }
                            } else {
                                System.out.println("ℹ️ Product remains active.");
                            }
                        }
                    }
                    break;




                

                case 5:
                    OrderService orderService = new OrderService();
                    List<OrderDetails> orders = orderService.viewSellerOrders(user.getUserId());

                    System.out.println("\n========== ORDERS ON MY PRODUCTS ==========");

                    if (orders.isEmpty()) {
                        System.out.println("❌ No orders found!");
                    } else {
                        System.out.printf("%-8s %-8s %-20s %-8s %-10s %-10s %-10s%n",
                                "ORDERID", "BUYERID", "PRODUCT", "QTY", "PRICE", "TOTAL", "STATUS");
                        System.out.println("-------------------------------------------------------------------------");

                        for (OrderDetails o : orders) {
                            System.out.printf("%-8d %-8d %-20s %-8d %-10.2f %-10.2f %-10s%n",
                                    o.getOrderId(),
                                    o.getBuyerId(),
                                    o.getProductName(),
                                    o.getQuantity(),
                                    o.getPrice(),
                                    o.getItemTotal(),
                                    o.getStatus());
                        }
                    }	
                    break;
                    
                case 6:
                    ReviewService reviewService = new ReviewService();
                    List<Review> reviews = reviewService.viewReviewsForSeller(user.getUserId());

                    System.out.println("\n======= REVIEWS ON YOUR PRODUCTS =======");

                    if (reviews.isEmpty()) {
                        System.out.println("⚠️ No reviews received yet.");
                    } else {
                        for (Review r : reviews) {
                            System.out.println("📦 Product ID: " + r.getProductId());
                            System.out.println("⭐ Rating: " + r.getRating());
                            System.out.println("📝 Review: " + r.getReviewText());
                            System.out.println("🕒 Date: " + r.getCreatedAt());
                            System.out.println("-------------------------------------");
                        }
                    }
                    break;


                case 7:
                    NotificationService notificationService = new NotificationService();
                    List<Notification> notifications =
                            notificationService.viewNotifications(user.getUserId());

                    System.out.println("\n========== NOTIFICATIONS ==========");

                    if (notifications.isEmpty()) {
                        System.out.println("🔔 No notifications found!");
                    } else {
                        for (Notification n : notifications) {
                            System.out.println("• " + n.getMessage()
                                    + "  [" + n.getCreatedAt() + "]");
                        }
                    }
                    break;
  
                case 8:
                    try {
                        System.out.print("Enter Old Password: ");
                        String oldPwd = sc.nextLine();

                        System.out.print("Enter New Password: ");
                        String newPwd = sc.nextLine();

                        AuthService authService = new AuthService();

                        // ✅ No boolean check
                        authService.changePassword(user.getUserId(), oldPwd, newPwd);

                        System.out.println("✅ Password changed successfully!");

                    } catch (ValidationException ve) {
                        System.out.println("❌ " + ve.getMessage());
                    } catch (InvalidCredentialsException ice) {
                        System.out.println("❌ " + ice.getMessage());
                    } catch (AuthException ae) {
                        System.out.println("❌ " + ae.getMessage());
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
