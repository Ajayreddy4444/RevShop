package Dao;

import Util.DBConnection;
import Model.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public void addNotification(int userId, String message) {

        String sql =
          "INSERT INTO notifications (notification_id, user_id, message) " +
          "VALUES (notifications_seq.NEXTVAL, ?, ?)";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();

            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Notification Error: " + e.getMessage());
        }
    }
    
    

    public List<Notification> getNotificationsByUserId(int userId) {

        List<Notification> list = new ArrayList<Notification>();

        String sql =
            "SELECT notification_id, user_id, message, created_at " +
            "FROM notifications WHERE user_id = ? " +
            "ORDER BY created_at DESC";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, userId);

            rs = ps.executeQuery();

            while (rs.next()) {
                Notification n = new Notification();
                n.setNotificationId(rs.getInt("notification_id"));
                n.setUserId(rs.getInt("user_id"));
                n.setMessage(rs.getString("message"));
                n.setCreatedAt(String.valueOf(rs.getTimestamp("created_at")));

                list.add(n);
            }

        } catch (Exception e) {
            System.out.println("Fetch Notifications Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return list;
    }

}
