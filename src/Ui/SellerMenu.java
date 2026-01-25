package Ui;

import Model.User;
import Model.Product;
import Model.OrderDetails;
import Service.ProductService;
import Service.OrderService;

import java.util.List;
import java.util.Scanner;

public class SellerMenu {

    public static void show(User user) {

        Scanner sc = new Scanner(System.in);
        ProductService productService = new ProductService();

        while (true) {
            System.out.println("\n===== SELLER MENU =====");
            System.out.println("Welcome, " + user.getName());
            System.out.println("1. Add Product");
            System.out.println("2. View My Products");
            System.out.println("3. View Orders");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {

                case 1:
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

                case 2:
                    List<Product> myProducts = productService.viewProductsBySeller(user.getUserId());

                    System.out.println("\n========== MY PRODUCTS ==========");

                    if (myProducts.isEmpty()) {
                        System.out.println("❌ You have not added any products yet.");
                    } else {
                        System.out.printf("%-5s %-20s %-15s %-10s %-10s %-10s%n",
                                "ID", "NAME", "CATEGORY", "MRP", "PRICE", "STOCK");
                        System.out.println("---------------------------------------------------------------------");

                        for (Product p : myProducts) {
                            System.out.printf("%-5d %-20s %-15s %-10.2f %-10.2f %-10d%n",
                                    p.getProductId(),
                                    p.getName(),
                                    p.getCategory(),
                                    p.getMrp(),
                                    p.getPrice(),
                                    p.getStock());
                        }
                    }
                    break;

                case 3:
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

                case 4:
                    System.out.println("✅ Logged out successfully!");
                    return;

                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
}
