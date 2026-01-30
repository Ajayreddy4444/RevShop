package Service;

import Model.CartItem;
import Model.Product;
import Exception.CartException;
import Exception.InvalidCartOperationException;

import java.util.ArrayList;
import java.util.List;

public class CartService {

    private List<CartItem> cart = new ArrayList<CartItem>();

    // Adds a product to cart
    public void addToCart(Product product, int quantity) {

        if (product == null) {
            throw new CartException("Product cannot be null");
        }

        if (quantity <= 0) {
            throw new InvalidCartOperationException("Quantity must be greater than zero");
        }

        if (quantity > product.getStock()) {
            throw new InvalidCartOperationException("Not enough stock available");
        }

        for (CartItem item : cart) {
            if (item.getProductId() == product.getProductId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        cart.add(new CartItem(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                quantity
        ));
    }

    // Displays cart contents
    public void viewCart() {
        if (cart.isEmpty()) {
            throw new CartException("Cart is empty!");
        }

        double total = 0;

        for (CartItem item : cart) {
            System.out.println(
                item.getProductName() + " | Qty: " +
                item.getQuantity() + " | Total: " + item.getTotal()
            );
            total += item.getTotal();
        }

        System.out.println("Grand Total: " + total);
    }


    // Removes a product from cart
    public void removeFromCart(int productId) {

        if (productId <= 0) {
            throw new InvalidCartOperationException("Invalid product ID");
        }

        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getProductId() == productId) {
                cart.remove(i);
                return;
            }
        }

        throw new CartException("Product not found in cart");
    }

    // Clears the cart
    public void clearCart() {
        cart.clear();
    }

    // Checks whether cart is empty
    public boolean isEmpty() {
        return cart.isEmpty();
    }

    // Returns all cart items
    public List<CartItem> getCartItems() {
        return cart;
    }
}
