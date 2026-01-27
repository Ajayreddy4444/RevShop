package Service;

import Dao.UserDAO;
import Model.User;

public class AuthService {

    private UserDAO userDAO = new UserDAO();

    // ============================================
    // ✅ REGISTER
    // ============================================
    public boolean register(User user) {

        if (user == null) {
            System.out.println("❌ Invalid user data!");
            return false;
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            System.out.println("❌ Email cannot be empty!");
            return false;
        }

        if (user.getPassword() == null || user.getPassword().length() < 4) {
            System.out.println("❌ Password must be at least 4 characters!");
            return false;
        }

        if (user.getPasswordHint() == null || user.getPasswordHint().trim().isEmpty()) {
            System.out.println("❌ Password hint is required!");
            return false;
        }

        return userDAO.registerUser(user);
    }

    // ============================================
    // ✅ LOGIN
    // ============================================
    public User login(String email, String password) {

        if (email == null || email.trim().isEmpty()) {
            System.out.println("❌ Email cannot be empty!");
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            System.out.println("❌ Password cannot be empty!");
            return null;
        }

        return userDAO.loginUser(email, password);
    }

    // ============================================
    // 🔐 CHANGE PASSWORD (Logged-in user)
    // ============================================
    public boolean changePassword(int userId, String oldPassword, String newPassword) {

        if (newPassword == null || newPassword.length() < 4) {
            System.out.println("❌ New password must be at least 4 characters!");
            return false;
        }

        return userDAO.changePassword(userId, oldPassword, newPassword);
    }

    // ============================================
    // 🔓 FORGOT PASSWORD
    // ============================================
    public boolean forgotPassword(String email, String hint, String newPassword) {

        if (email == null || email.trim().isEmpty()) {
            System.out.println("❌ Email cannot be empty!");
            return false;
        }

        if (hint == null || hint.trim().isEmpty()) {
            System.out.println("❌ Password hint cannot be empty!");
            return false;
        }

        if (newPassword == null || newPassword.length() < 4) {
            System.out.println("❌ New password must be at least 4 characters!");
            return false;
        }

        return userDAO.resetPassword(email, hint, newPassword);
    }
    
    
}
