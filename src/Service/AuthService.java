package Service;

import Dao.UserDAO;
import Model.User;
import Exception.AuthException;
import Exception.InvalidCredentialsException;
import Exception.ValidationException;
import org.apache.log4j.Logger;

/**
 * Provides authentication-related services such as
 * user registration, login, password change, and password recovery.
 *
 * <p>
 * This service acts as an intermediary between the UI layer
 * and the data access layer ({@link UserDAO}).
 * It is responsible for validating user input, enforcing
 * business rules, and delegating persistence operations
 * to the DAO.
 * </p>
 */
public class AuthService {

    private static final Logger logger =
            Logger.getLogger(AuthService.class);

    private UserDAO userDAO;

    /**
     * Default constructor used during normal application execution.
     *
     * <p>
     * Initializes the {@link UserDAO} internally.
     * </p>
     */
    public AuthService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Parameterized constructor used mainly for unit testing.
     *
     * <p>
     * Allows injection of a mock {@link UserDAO} using Mockito.
     * </p>
     *
     * @param userDAO the UserDAO implementation to be used
     */
    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Validates whether the given email follows a basic valid structure.
     *
     * <p>
     * Validation rules:
     * <ul>
     *   <li>Email must not be null</li>
     *   <li>Email must contain '@'</li>
     *   <li>Email must contain '.' after '@'</li>
     *   <li>Email must not end with '.'</li>
     *   <li>Email must not contain spaces</li>
     * </ul>
     * </p>
     *
     * @param email the email address to validate
     * @return {@code true} if email format is valid, otherwise {@code false}
     */
    private boolean isValidEmail(String email) {

        if (email == null) return false;

        email = email.trim();

        int atIndex = email.indexOf('@');
        if (atIndex <= 0) return false;

        int dotIndex = email.indexOf('.', atIndex);
        if (dotIndex == -1) return false;

        if (dotIndex == email.length() - 1) return false;

        if (email.contains(" ")) return false;

        return true;
    }

    /**
     * Registers a new user in the system.
     *
     * <p>
     * This method performs the following steps:
     * <ul>
     *   <li>Validates user object and mandatory fields</li>
     *   <li>Validates email format</li>
     *   <li>Validates password rules</li>
     *   <li>Ensures password and hint are not the same</li>
     *   <li>Validates user role (BUYER or SELLER)</li>
     *   <li>Persists the user using {@link UserDAO}</li>
     * </ul>
     * </p>
     *
     * @param user the user to be registered
     * @return {@code true} if registration is successful
     *
     * @throws ValidationException if validation fails
     * @throws AuthException if registration fails at DAO layer
     */
    public boolean register(User user) {

        logger.info("Register request received");

        if (user == null) {
            throw new ValidationException("User data cannot be null");
        }

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new ValidationException("Name cannot be empty");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }

        if (!isValidEmail(user.getEmail())) {
            throw new ValidationException("Invalid email format");
        }

        if (user.getPassword() == null || user.getPassword().length() < 4) {
            throw new ValidationException(
                    "Password must be at least 4 characters"
            );
        }

        if (user.getPasswordHint() == null ||
                user.getPasswordHint().trim().isEmpty()) {
            throw new ValidationException("Password hint is required");
        }

        if (user.getPassword().equals(user.getPasswordHint())) {
            throw new ValidationException(
                    "Password and hint cannot be the same"
            );
        }

        if (user.getRole() == null ||
                !(user.getRole().equalsIgnoreCase("BUYER")
                || user.getRole().equalsIgnoreCase("SELLER"))) {
            throw new ValidationException(
                    "Role must be BUYER or SELLER"
            );
        }

        boolean registered = userDAO.registerUser(user);

        if (!registered) {
            throw new AuthException("Registration failed");
        }

        logger.info("User registered successfully: " + user.getEmail());
        return true;
    }

    /**
     * Authenticates a user using email and password.
     *
     * <p>
     * This method:
     * <ul>
     *   <li>Validates email and password</li>
     *   <li>Checks credentials against the database</li>
     * </ul>
     * </p>
     *
     * @param email the user's email
     * @param password the user's password
     * @return the authenticated {@link User}
     *
     * @throws ValidationException if input validation fails
     * @throws InvalidCredentialsException if credentials are incorrect
     */
    public User login(String email, String password) {

        logger.info("Login attempt for email: " + email);

        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }

        if (!isValidEmail(email)) {
            throw new ValidationException("Invalid email format");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("Password cannot be empty");
        }

        if (password.length() < 4) {
            throw new ValidationException(
                    "Password must be at least 4 characters"
            );
        }

        User user = userDAO.loginUser(email, password);

        if (user == null) {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        logger.info("Login successful for email: " + email);
        return user;
    }

    /**
     * Changes the password of an existing user.
     *
     * @param userId the user's ID
     * @param oldPassword the current password
     * @param newPassword the new password to be set
     * @return {@code true} if password change is successful
     *
     * @throws ValidationException if new password is invalid
     * @throws InvalidCredentialsException if old password is incorrect
     */
    public boolean changePassword(int userId,
                                  String oldPassword,
                                  String newPassword) {

        if (newPassword == null || newPassword.length() < 4) {
            throw new ValidationException(
                    "New password must be at least 4 characters"
            );
        }

        boolean changed =
                userDAO.changePassword(userId, oldPassword, newPassword);

        if (!changed) {
            throw new InvalidCredentialsException(
                    "Old password is incorrect"
            );
        }

        logger.info("Password changed successfully for userId: " + userId);
        return true;
    }

    /**
     * Resets the password using email and password hint.
     *
     * <p>
     * This method verifies the user's identity using
     * email and password hint before updating the password.
     * </p>
     *
     * @param email the registered email
     * @param hint the password hint
     * @param newPassword the new password
     * @return {@code true} if password reset is successful
     *
     * @throws ValidationException if input validation fails
     * @throws InvalidCredentialsException if details are incorrect
     */
    public boolean forgotPassword(String email,
                                  String hint,
                                  String newPassword) {

        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }

        if (!isValidEmail(email)) {
            throw new ValidationException("Invalid email format");
        }

        if (hint == null || hint.trim().isEmpty()) {
            throw new ValidationException("Password hint cannot be empty");
        }

        if (newPassword == null || newPassword.length() < 4) {
            throw new ValidationException(
                    "New password must be at least 4 characters"
            );
        }

        boolean reset =
                userDAO.resetPassword(email, hint, newPassword);

        if (!reset) {
            throw new InvalidCredentialsException(
                    "Email or password hint is incorrect"
            );
        }

        logger.info("Password reset successful for email: " + email);
        return true;
    }
}
