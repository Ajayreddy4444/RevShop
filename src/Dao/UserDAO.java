package Dao;

import Model.User;
import Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

	/**
	 * Registers a new user with a password hint.
	 *
	 * @param user user details
	 * @return {@code true} if registration succeeds
	 */
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

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Register Error: " + e.getMessage());
            return false;

        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

	/**
	 * Authenticates a user using email and password.
	 *
	 * @param email user email
	 * @param password user password
	 * @return {@link User} if credentials are valid, otherwise {@code null}
	 */
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
            System.out.println("Login Error: " + e.getMessage());

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return null;
    }

	/**
	 * Changes password for a logged-in user.
	 *
	 * @param userId user ID
	 * @param oldPassword current password
	 * @param newPassword new password
	 * @return {@code true} if password is changed
	 */
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

            ps = con.prepareStatement(checkSql);
            ps.setInt(1, userId);
            ps.setString(2, oldPassword);
            rs = ps.executeQuery();

            if (!rs.next()) {
                return false;
            }

            rs.close();
            ps.close();

            ps = con.prepareStatement(updateSql);
            ps.setString(1, newPassword);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Change Password Error: " + e.getMessage());
            return false;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

	/**
	 * Resets password using email and password hint.
	 *
	 * @param email user email
	 * @param hint password hint
	 * @param newPassword new password
	 * @return {@code true} if reset succeeds
	 */
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

            ps = con.prepareStatement(checkSql);
            ps.setString(1, email);
            ps.setString(2, hint);
            rs = ps.executeQuery();

            if (!rs.next()) {
                return false;
            }

            rs.close();
            ps.close();

            ps = con.prepareStatement(updateSql);
            ps.setString(1, newPassword);
            ps.setString(2, email);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Reset Password Error: " + e.getMessage());
            return false;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
    
    
    /**
     * Retrieves buyer ID mapped to a user ID.
     *
     * @param userId user ID
     * @return buyer ID or {@code -1} if not found
     */

    public int getBuyerIdByUserId(int userId) {

        String sql = "SELECT buyer_id FROM buyers WHERE user_id = ?";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("buyer_id");
            }

        } catch (Exception e) {
            return -1;
        }

        return -1;
    }

}
