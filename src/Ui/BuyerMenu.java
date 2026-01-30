package Ui;

import Exception.AuthException;
import Exception.CartException;
import Exception.InvalidCredentialsException;
import Exception.InvalidWishlistOperationException;
import Exception.OrderException;
import Exception.ProductException;
import Exception.ReviewException;
import Exception.ValidationException;
import Exception.WishlistException;
import Model.User;
import Model.Product;
import Model.OrderDetails;
import Service.AuthService;
import Service.ProductService;
import Service.CartService;
import Service.OrderService;
import Service.NotificationService;
import Service.WishlistService;
import Model.Notification;
import java.util.List;
import Service.ReviewService;
import Model.Review;
import java.util.Scanner;

public class BuyerMenu {

	static ReviewService reviewService = new ReviewService();

    public static void show(User user) {

        ProductService productService = new ProductService();
        CartService cartService = new CartService();   // cart persists
        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== BUYER MENU =====");
            System.out.println("1. View All Products (Add to Cart)");
            System.out.println("2. Search Products by Category");
            System.out.println("3. Search Products by Keyword");
            System.out.println("4. Add to Wishlist");
            System.out.println("5. View Wishlist");
            System.out.println("6. Remove from Wishlist");
            System.out.println("7. View Cart");
            System.out.println("8. Remove Item from Cart");
            System.out.println("9. Checkout (Buy Now)");
            System.out.println("10. View Orders");
            System.out.println("11. Cancel Order");
            System.out.println("12. View Notifications");
            System.out.println("13. Change Password");
            System.out.println("14. Add Product Review");
            System.out.println("15. View Product Reviews");
            System.out.println("16. Logout");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

            case 1:
                try {
                    List<Product> products = productService.viewAllProducts();

                    System.out.println("\n==================== PRODUCT LIST ====================\n");

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

                    // ✅ ASK USER IF THEY WANT TO ADD TO CART
                    System.out.print("\nDo you want to add a product to cart? (yes/no): ");
                    String choiceAdd = sc.nextLine();

                    if (choiceAdd.equalsIgnoreCase("yes")) {

                        System.out.print("Enter Product ID: ");
                        int productId = sc.nextInt();

                        System.out.print("Enter Quantity: ");
                        int qty = sc.nextInt();
                        sc.nextLine(); // consume newline

                        Product selectedProduct = productService.getProductById(productId);

                        if (qty <= 0) {
                            throw new ValidationException("Quantity must be greater than zero");
                        }

                        if (qty > selectedProduct.getStock()) {
                            throw new ValidationException("Not enough stock available");
                        }

                        cartService.addToCart(selectedProduct, qty);
                        System.out.println("🛒 Product added to cart!");
                    }

                    // if NO → automatically returns to menu

                } catch (ProductException p) {
                    System.out.println("❌ " + p.getMessage());
                } catch (ValidationException v) {
                    System.out.println("❌ " + v.getMessage());
                } catch (CartException c) {
                    System.out.println("❌ " + c.getMessage());
                }
                break;





            case 2:
                try {
                    List<String> categories = productService.viewAllCategories();

                    System.out.println("\n===== AVAILABLE CATEGORIES =====");
                    for (int i = 0; i < categories.size(); i++) {
                        System.out.println((i + 1) + ". " + categories.get(i));
                    }

                    System.out.print("Select Category Number: ");
                    int catChoice = sc.nextInt();
                    sc.nextLine();

                    if (catChoice < 1 || catChoice > categories.size()) {
                        System.out.println("❌ Invalid category selection!");
                        break;
                    }

                    String selectedCategory = categories.get(catChoice - 1);
                    List<Product> categoryProducts =
                            productService.viewProductsByCategory(selectedCategory);

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

                    // ✅ ADD TO CART OPTION
                    System.out.print("\nAdd product to cart? (yes/no): ");
                    String addChoice = sc.nextLine();

                    if (addChoice.equalsIgnoreCase("yes")) {
                        System.out.print("Enter Product ID: ");
                        int pid = sc.nextInt();

                        System.out.print("Enter Quantity: ");
                        int qty = sc.nextInt();
                        sc.nextLine();

                        Product product = productService.getProductById(pid);
                        cartService.addToCart(product, qty);
                    }

                } catch (ProductException pe) {
                    System.out.println("❌ " + pe.getMessage());
                }
                break;



            case 3:
                try {
                    System.out.print("Enter Keyword: ");
                    String key = sc.nextLine();

                    List<Product> keyProducts = productService.searchByKeyword(key);

                    System.out.println("\n===== SEARCH RESULTS =====");

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

                    // ✅ ADD TO CART OPTION
                    System.out.print("\nAdd product to cart? (yes/no): ");
                    String addChoice = sc.nextLine();

                    if (addChoice.equalsIgnoreCase("yes")) {
                        System.out.print("Enter Product ID: ");
                        int pid = sc.nextInt();

                        System.out.print("Enter Quantity: ");
                        int qty = sc.nextInt();
                        sc.nextLine();

                        Product product = productService.getProductById(pid);
                        cartService.addToCart(product, qty);
                    }

                } catch (ProductException pe) {
                    System.out.println("❌ " + pe.getMessage());
                }
                break;


                
            case 4:
                System.out.print("Enter Product ID to add to wishlist: ");
                int favId = sc.nextInt();
                sc.nextLine();

                WishlistService wishlistService = new WishlistService();

                if (wishlistService.add(user.getUserId(), favId)) {
                    System.out.println("❤️ Added to wishlist!");
                } else {
                    System.out.println("❌ Failed to add to wishlist.");
                }
                break;


                

            	    case 5:
            	        try {
            	            WishlistService ws = new WishlistService();
            	            List<Product> wishlist = ws.view(user.getUserId());

            	            System.out.println("\n===== MY WISHLIST =====");
            	            for (Product p : wishlist) {
            	                System.out.println(p.getProductId() + " | " +
            	                                   p.getName() + " | ₹" + p.getPrice());
            	            }

            	        } catch (WishlistException w)  {
            	            System.out.println(w.getMessage());
            	        }
            	          catch(ValidationException v)
            	          {
              	            System.out.println(v.getMessage());
            	          }
            	        break;


            	    case 6:
            	        try {
            	            System.out.print("Enter Product ID to remove: ");
            	            int removeId = sc.nextInt();
            	            sc.nextLine();

            	            WishlistService wsRemove = new WishlistService();
            	            wsRemove.remove(user.getUserId(), removeId);

            	            System.out.println("🗑️ Removed from wishlist!");

            	        } 
            	        catch (WishlistException w)  {
            	            System.out.println(w.getMessage());
            	        }
            	         catch(ValidationException v)
            	         {
              	            System.out.println(v.getMessage());
            	          }
            	        
            	        break;


                case 7:
                    try {
                        cartService.viewCart();
                    } catch (CartException ce) {
                        System.out.println("⚠️ " + ce.getMessage());
                    }
                    break;
                    
                case 8:
                    try {
                        System.out.print("Enter Product ID to remove: ");
                        int idToRemove = sc.nextInt();
                        sc.nextLine();

                        cartService.removeFromCart(idToRemove);
                        System.out.println("🗑️ Removed from cart!");

                    } catch (CartException ce) {
                        System.out.println("❌ " + ce.getMessage());
                    }
                    break;

                case 9:
                    try {
                        // 1️⃣ Show cart (throws CartException if empty)
                        cartService.viewCart();

                        // 2️⃣ Shipping address
                        System.out.print("Enter Shipping Address: ");
                        String address = sc.nextLine();

                        if (address == null || address.trim().isEmpty()) {
                            throw new ValidationException("Shipping address cannot be empty");
                        }

                        // 3️⃣ Payment options
                        System.out.println("\n===== PAYMENT OPTIONS =====");
                        System.out.println("1. Cash on Delivery (COD)");
                        System.out.println("2. UPI");
                        System.out.println("3. Card");
                        System.out.print("Select Payment Method: ");

                        int payChoice = sc.nextInt();
                        sc.nextLine(); // consume newline

                        String paymentMethod = null;

                        if (payChoice == 1) {
                            paymentMethod = "COD";
                            System.out.println("✅ COD selected");

                        } else if (payChoice == 2) {
                            System.out.print("Enter UPI ID: ");
                            String upi = sc.nextLine();

                            if (upi == null || upi.trim().isEmpty()) {
                                throw new ValidationException("Invalid UPI ID");
                            }
                            paymentMethod = "UPI";
                            System.out.println("✅ Payment successful via UPI");

                        } else if (payChoice == 3) {
                            System.out.print("Enter Card Last 4 Digits: ");
                            String card = sc.nextLine();

                            if (card == null || card.length() != 4) {
                                throw new ValidationException("Invalid card details");
                            }
                            paymentMethod = "CARD";
                            System.out.println("✅ Payment successful via Card");

                        } else {
                            throw new ValidationException("Invalid payment option");
                        }

                        // 4️⃣ Confirm checkout
                        System.out.print("\nConfirm Checkout? (yes/no): ");
                        String confirm = sc.nextLine();

                        if (!confirm.equalsIgnoreCase("yes")) {
                            System.out.println("❌ Checkout cancelled");
                            break;
                        }

                        // 5️⃣ Place order
                        OrderService orderService = new OrderService();
                        int orderId = orderService.checkout(
                                user.getUserId(),
                                cartService.getCartItems(),
                                address,
                                paymentMethod
                        );

                        System.out.println("✅ Order placed successfully! Order ID: " + orderId);
                        cartService.clearCart();

                    } catch (CartException c) {

                        System.out.println("❌ " + c.getMessage());
                    }
                      catch (ValidationException v) {

                    	  System.out.println("❌ " + v.getMessage());
                    }
                      catch (OrderException o) {

                    	  System.out.println("❌ " + o.getMessage());
                    }
           
                    break;



                case 10:
                    OrderService orderSErvice = new OrderService();
                    List<OrderDetails> myOrders = orderSErvice.viewBuyerOrders(user.getUserId());

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

                case 11:
                    System.out.print("Enter Order ID to cancel: ");
                    int cancelId = sc.nextInt();
                    sc.nextLine();

                    OrderService cancelService = new OrderService();
                    try {
                    	cancelService.cancelOrder(user.getUserId(), cancelId);
                        System.out.println("✅ Order cancelled successfully!");

                    } catch (OrderException e) {
                        System.out.println("❌ " + e.getMessage());
                    }
                    break;
                    
                case 12:
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

                case 13:
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
                        ve.getMessage();
                    } catch (InvalidCredentialsException ice) {
                        ice.getMessage();
                    } catch (AuthException ae) {
                        ae.getMessage();
                    }

                    break;


                case 14:
                    try {
                        System.out.println("\n===== ADD PRODUCT REVIEW =====");

                        System.out.print("Enter Product ID: ");
                        int productId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Rating (1-5): ");
                        int rating = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Review Text: ");
                        String reviewText = sc.nextLine();

                        reviewService.addReview(
                                user.getUserId(),
                                productId,
                                rating,
                                reviewText
                        );

                        System.out.println("✅ Review added successfully!");

                    } catch (ValidationException ve) {
                        System.out.println("❌ " + ve.getMessage());
                    } catch (ReviewException re) {
                        System.out.println("❌ " + re.getMessage());
                    }
                    break;

                    
                case 15:
                    try {
                        System.out.print("Enter Product ID to view reviews: ");
                        int reviewProductId = sc.nextInt();
                        sc.nextLine();

                        List<Review> reviews =
                                reviewService.viewReviewsByProduct(reviewProductId);

                        System.out.println("\n======= PRODUCT REVIEWS =======");

                        if (reviews.isEmpty()) {
                            System.out.println("⚠️ No reviews found for this product.");
                        } else {
                            for (Review r : reviews) {
                                System.out.println("⭐ Rating: " + r.getRating());
                                System.out.println("📝 Review: " + r.getReviewText());
                                System.out.println("🕒 Date: " + r.getCreatedAt());
                                System.out.println("-------------------------------");
                            }
                        }

                    } catch (ValidationException ve) {
                        System.out.println("❌ " + ve.getMessage());
                    }
                    break;


    
                    
                case 16:
                    System.out.println("✅ Logged out successfully!");
                    return;

                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
}
