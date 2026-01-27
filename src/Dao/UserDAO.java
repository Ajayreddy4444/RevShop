package Dao;

import Model.User;
import Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    // ============================================
    // ✅ REGISTER USER (with password hint)
    // ============================================
    public boolean registerUser(User user) {

        String sql =
            "INSERT INTO users (user_id, name, email, password, role, password_hint) " +
            "VALUES (users_seq.NEXTVAL, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getPasswordHint());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            System.out.println("❌ Register Error: " + e.getMessage());
            return false;

        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    // ============================================
    // ✅ LOGIN USER
    // ============================================
    public User loginUser(String email, String password) {

        String sql =
            "SELECT user_id, name, email, role FROM users " +
            "WHERE email = ? AND password = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                return user;
            }

        } catch (Exception e) {
            System.out.println("❌ Login Error: " + e.getMessage());

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return null;
    }

    // ============================================
    // 🔐 CHANGE PASSWORD (Logged-in user)
    // ============================================
    public boolean changePassword(int userId, String oldPassword, String newPassword) {

        String checkSql =
            "SELECT user_id FROM users WHERE user_id = ? AND password = ?";

        String updateSql =
            "UPDATE users SET password = ? WHERE user_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();

            // 1️⃣ Verify old password
            ps = con.prepareStatement(checkSql);
            ps.setInt(1, userId);
            ps.setString(2, oldPassword);

            rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Old password is incorrect!");
                return false;
            }

            rs.close();
            ps.close();

            // 2️⃣ Update password
            ps = con.prepareStatement(updateSql);
            ps.setString(1, newPassword);
            ps.setInt(2, userId);

            int updated = ps.executeUpdate();
            return updated > 0;

        } catch (Exception e) {
            System.out.println("❌ Change Password Error: " + e.getMessage());
            return false;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    // ============================================
    // 🔓 FORGOT PASSWORD (Email + Hint)
    // ============================================
    public boolean resetPassword(String email, String hint, String newPassword) {

        String checkSql =
            "SELECT user_id FROM users WHERE email = ? AND password_hint = ?";

        String updateSql =
            "UPDATE users SET password = ? WHERE email = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();

            // 1️⃣ Verify email + hint
            ps = con.prepareStatement(checkSql);
            ps.setString(1, email);
            ps.setString(2, hint);

            rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Invalid email or password hint!");
                return false;
            }

            rs.close();
            ps.close();

            // 2️⃣ Update password
            ps = con.prepareStatement(updateSql);
            ps.setString(1, newPassword);
            ps.setString(2, email);

            int updated = ps.executeUpdate();
            return updated > 0;

        } catch (Exception e) {
            System.out.println("❌ Forgot Password Error: " + e.getMessage());
            return false;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
    
    
}
