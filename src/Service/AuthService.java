package Service;

import Dao.UserDAO;
import Model.User;
import Exception.AuthException;
import Exception.InvalidCredentialsException;
import Exception.ValidationException;
import org.apache.log4j.Logger;

public class AuthService {

    private static final Logger logger =
            Logger.getLogger(AuthService.class);

    private UserDAO userDAO = new UserDAO();

    // Registers a new buyer or seller
    public boolean register(User user) {

        logger.info("Register request received");

        if (user == null) {
            logger.warn("Registration failed: user object is null");
            throw new ValidationException("User data cannot be null");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            logger.warn("Registration failed: email is empty");
            throw new ValidationException("Email cannot be empty");
        }

        if (user.getPassword() == null || user.getPassword().length() < 4) {
            logger.warn("Registration failed: password too short");
            throw new ValidationException("Password must be at least 4 characters");
        }

        if (user.getPasswordHint() == null || user.getPasswordHint().trim().isEmpty()) {
            logger.warn("Registration failed: password hint missing");
            throw new ValidationException("Password hint is required");
        }

        boolean registered = userDAO.registerUser(user);

        if (!registered) {
            logger.error("Registration failed for email: " + user.getEmail());
            throw new AuthException("Registration failed");
        }

        logger.info("User registered successfully: " + user.getEmail());
        return true;
    }

    // Logs in a user using email and password
    public User login(String email, String password) {

        logger.info("Login attempt for email: " + email);

        if (email == null || email.trim().isEmpty()) {
            logger.warn("Login failed: email empty");
            throw new ValidationException("Email cannot be empty");
        }

        if (password == null || password.trim().isEmpty()) {
            logger.warn("Login failed: password empty");
            throw new ValidationException("Password cannot be empty");
        }

        User user = userDAO.loginUser(email, password);

        if (user == null) {
            logger.warn("Login failed: invalid credentials for email: " + email);
            throw new InvalidCredentialsException("Invalid email or password");
        }

        logger.info("Login successful for email: " + email);
        return user;
    }

    // Changes password for logged-in user
    public boolean changePassword(int userId, String oldPassword, String newPassword) {

        logger.info("Change password request for userId: " + userId);

        if (newPassword == null || newPassword.length() < 4) {
            logger.warn("Password change failed: new password too short");
            throw new ValidationException("New password must be at least 4 characters");
        }

        boolean changed = userDAO.changePassword(userId, oldPassword, newPassword);

        if (!changed) {
            logger.warn("Password change failed: old password incorrect for userId: " + userId);
            throw new InvalidCredentialsException("Old password is incorrect");
        }

        logger.info("Password changed successfully for userId: " + userId);
        return true;
    }

    // Resets password using email and password hint
    public boolean forgotPassword(String email, String hint, String newPassword) {

        logger.info("Forgot password request for email: " + email);

        if (email == null || email.trim().isEmpty()) {
            logger.warn("Forgot password failed: email empty");
            throw new ValidationException("Email cannot be empty");
        }

        if (hint == null || hint.trim().isEmpty()) {
            logger.warn("Forgot password failed: hint empty");
            throw new ValidationException("Password hint cannot be empty");
        }

        if (newPassword == null || newPassword.length() < 4) {
            logger.warn("Forgot password failed: new password too short");
            throw new ValidationException("New password must be at least 4 characters");
        }

        boolean reset = userDAO.resetPassword(email, hint, newPassword);

        if (!reset) {
            logger.warn("Forgot password failed: invalid hint or email: " + email);
            throw new InvalidCredentialsException("Email or password hint is incorrect");
        }

        logger.info("Password reset successful for email: " + email);
        return true;
    }
}
