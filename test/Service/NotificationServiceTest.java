package Service;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import Dao.NotificationDAO;

public class NotificationServiceTest {

    private NotificationService notificationService;
    private NotificationDAO notificationDAOMock;
    

    @Before
    public void setUp() {
        notificationDAOMock = mock(NotificationDAO.class);
        notificationService = new NotificationService(notificationDAOMock);
    }

    @Test
    public void testSendNotification() {
        notificationService.notifyUser(1, "Order placed");

        verify(notificationDAOMock, times(1))
                .addNotification(1, "Order placed");
    }
}
