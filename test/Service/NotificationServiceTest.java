package Service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Model.Notification;

public class NotificationServiceTest {

    private NotificationService notificationService;

    @Before
    public void setUp() {
        notificationService = new NotificationService();
    }

    // ===============================
    // 1️⃣ Test: Add Notification
    // ===============================
    @Test
    public void testNotifyUser() {

        int userId = 1; // make sure this user exists in DB
        String message = "JUnit Test Notification";

        // no return value, just ensure no exception
        notificationService.notifyUser(userId, message);

        List<Notification> notifications =
                notificationService.viewNotifications(userId);

        boolean found = false;

        for (Notification n : notifications) {
            if (n.getMessage().equals(message)) {
                found = true;
                break;
            }
        }

        assertTrue("Notification should be added", found);
    }

    // ===============================
    // 2️⃣ Test: View Notifications
    // ===============================
    @Test
    public void testViewNotifications() {

        int userId = 1;

        List<Notification> notifications =
                notificationService.viewNotifications(userId);

        assertNotNull(notifications);
        // list may be empty, but should never be null
    }
}
