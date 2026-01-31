package Service;

import Model.CartItem;
import Model.Product;
import Exception.CartException;
import Exception.InvalidCartOperationException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Provides cart-related operations such as adding products,
 * removing products, viewing cart contents, and clearing the cart.
 *
 * <p>
 * This service maintains an in-memory cart for a user session
 * and enforces business rules related to quantity and stock.
 * </p>
 */
public class CartService {

    private static final Logger logger =
            Logger.getLogger(CartService.class);

    private List<CartItem> cart = new ArrayList<CartItem>();

    /**
     * Adds a product to the cart.
     *
     * <p>
     * This method performs the following steps:
     * <ul>
     *   <li>Validates product object</li>
     *   <li>Validates quantity</li>
     *   <li>Checks stock availability</li>
     *   <li>If product already exists in cart, updates quantity</li>
     *   <li>Otherwise, adds a new cart item</li>
     * </ul>
     * </p>
     *
     * @param product the product to be added
     * @param quantity the quantity requested
     *
     * @throws CartException if product is null
     * @throws InvalidCartOperationException if quantity or stock is invalid
     */
    public void addToCart(Product product, int quantity) {

        logger.info("Add to cart request | Product: " +
                (product != null ? product.getName() : "null") +
                " | Quantity: " + quantity);

        if (product == null) {
            throw new CartException("Product cannot be null");
        }

        if (quantity <= 0) {
            throw new InvalidCartOperationException(
                    "Quantity must be greater than zero");
        }

        if (quantity > product.getStock()) {
            throw new InvalidCartOperationException(
                    "Not enough stock available");
        }

        for (CartItem item : cart) {
            if (item.getProductId() == product.getProductId()) {
                item.setQuantity(item.getQuantity() + quantity);
                logger.info("Cart quantity updated for product ID: " +
                        product.getProductId());
                return;
            }
        }

        cart.add(new CartItem(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                quantity
        ));

        logger.info("Product added to cart | Product ID: " +
                product.getProductId());
    }

    /**
     * Displays the contents of the cart.
     *
     * @throws CartException if the cart is empty
     */
    public void viewCart() {

        logger.info("View cart requested");

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

    /**
     * Removes a product from the cart using product ID.
     *
     * @param productId the product ID to remove
     *
     * @throws InvalidCartOperationException if product ID is invalid
     * @throws CartException if product is not found in cart
     */
    public void removeFromCart(int productId) {

        if (productId <= 0) {
            throw new InvalidCartOperationException("Invalid product ID");
        }

        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getProductId() == productId) {
                cart.remove(i);
                logger.info("Product removed from cart | Product ID: " + productId);
                return;
            }
        }

        throw new CartException("Product not found in cart");
    }

    /**
     * Clears all items from the cart.
     */
    public void clearCart() {
        cart.clear();
        logger.info("Cart cleared");
    }

    /**
     * Checks whether the cart is empty.
     *
     * @return {@code true} if cart is empty, otherwise {@code false}
     */
    public boolean isEmpty() {
        return cart.isEmpty();
    }

    /**
     * Returns all items currently in the cart.
     *
     * @return list of {@link CartItem}
     */
    public List<CartItem> getCartItems() {
        return cart;
    }
}
