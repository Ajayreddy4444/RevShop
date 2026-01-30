package Model;

import static org.junit.Assert.*;
import org.junit.Test;

public class NotificationTest {

    // Tests getters and setters of Notification model
    @Test
    public void testNotificationModel() {

        Notification n = new Notification();
        n.setNotificationId(1);
        n.setUserId(10);
        n.setMessage("Order placed");
        n.setCreatedAt("2026-01-01");

        assertEquals(1, n.getNotificationId());
        assertEquals(10, n.getUserId());
        assertEquals("Order placed", n.getMessage());
        assertEquals("2026-01-01", n.getCreatedAt());
    }
}
