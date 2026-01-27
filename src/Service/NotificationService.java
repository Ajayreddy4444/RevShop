package Service;

import Dao.NotificationDAO;
import Model.Notification;
import java.util.List;

public class NotificationService {

    private NotificationDAO notificationDAO = new NotificationDAO();

    // 🔔 Add notification
    public void notifyUser(int userId, String message) {
        try {
            notificationDAO.addNotification(userId, message);
        } catch (Exception e) {
            System.out.println("⚠️ Notification skipped: " + e.getMessage());
        }
    }

    // 📥 View notifications
    public List<Notification> viewNotifications(int userId) {
        return notificationDAO.getNotificationsByUserId(userId);
    }
}
