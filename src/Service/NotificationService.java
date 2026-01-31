package Service;

import Dao.NotificationDAO;
import Model.Notification;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Manages user notifications.
 */
public class NotificationService {

    private static final Logger logger =
            Logger.getLogger(NotificationService.class);

    private NotificationDAO notificationDAO;

    /**
     * Default constructor.
     */
    public NotificationService() {
        this.notificationDAO = new NotificationDAO();
    }

    /**
     * Constructor for Mockito testing.
     *
     * @param notificationDAO mocked DAO
     */
    public NotificationService(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    /**
     * Sends a notification to a user.
     *
     * @param userId user ID
     * @param message notification message
     */
    public void notifyUser(int userId, String message) {
        notificationDAO.addNotification(userId, message);
    }

    /**
     * Retrieves notifications for a user.
     *
     * @param userId user ID
     * @return list of notifications
     */
    public List<Notification> viewNotifications(int userId) {
        return notificationDAO.getNotificationsByUserId(userId);
    }
}
