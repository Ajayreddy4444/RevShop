package Dao;

import Model.User;
import Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    // ✅ Register Buyer or Seller
    public boolean registerUser(User user) {

        String sql = "INSERT INTO users(name, email, password, role) VALUES (?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            System.out.println("Register Error: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (Exception e) { }

            try {
                if (con != null) con.close();
            } catch (Exception e) { }
        }

        return false;
    }

    // ✅ Login Buyer or Seller
    public User loginUser(String email, String password) {

        String sql = "SELECT user_id, name, email, role FROM users WHERE email=? AND password=?";

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
            try {
                if (rs != null) rs.close();
            } catch (Exception e) { }

            try {
                if (ps != null) ps.close();
            } catch (Exception e) { }

            try {
                if (con != null) con.close();
            } catch (Exception e) { }
        }

        return null;
    }
}
