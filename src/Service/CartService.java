package Service;

import Model.CartItem;
import Model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartService {

    private List<CartItem> cart = new ArrayList<CartItem>();

    // Add item to cart
    public void addToCart(Product product, int quantity) {

        // If product already exists in cart -> increase quantity
        for (CartItem item : cart) {
            if (item.getProductId() == product.getProductId()) {
                item.setQuantity(item.getQuantity() + quantity);
                System.out.println("Quantity updated in cart!");
                return;
            }
        }

        // New item
        cart.add(new CartItem(product.getProductId(), product.getName(), product.getPrice(), quantity));
        System.out.println("Product added to cart!");
    }

    public void viewCart() {

        if (cart.isEmpty()) {
            System.out.println("Cart is empty!");
            return;
        }

        System.out.println("\n========== YOUR CART ==========");
        System.out.printf("%-5s %-20s %-10s %-10s %-10s%n",
                "ID", "NAME", "PRICE", "QTY", "TOTAL");
        System.out.println("------------------------------------------------");

        double grandTotal = 0;

        for (CartItem item : cart) {
            System.out.printf("%-5d %-20s %-10.2f %-10d %-10.2f%n",
                    item.getProductId(),
                    item.getProductName(),
                    item.getPrice(),
                    item.getQuantity(),
                    item.getTotal());

            grandTotal += item.getTotal();
        }

        System.out.println("------------------------------------------------");
        System.out.println("Grand Total = Rs. " + grandTotal);
    }

    public void removeFromCart(int productId) {

        boolean removed = false;

        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getProductId() == productId) {
                cart.remove(i);
                removed = true;
                break;
            }
        }

        if (removed) {
            System.out.println("Removed from cart successfully.");
        } else {
            System.out.println("Product not found in cart.");
        }
    }

    public void clearCart() {
        cart.clear();
    }

    public boolean isEmpty() {
        return cart.isEmpty();
    }

    public List<CartItem> getCartItems() {
        return cart;
    }
}
