package Service;

import Dao.NotificationDAO;
import Model.Notification;

import java.util.List;

public class NotificationService {

    private NotificationDAO notificationDAO = new NotificationDAO();

    // Sends a notification to user
    public void notifyUser(int userId, String message) {
        notificationDAO.addNotification(userId, message);
    }

    // Fetches notifications for user
    public List<Notification> viewNotifications(int userId) {
        return notificationDAO.getNotificationsByUserId(userId);
    }
}
