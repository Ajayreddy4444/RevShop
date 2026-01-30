package Model;

import static org.junit.Assert.*;
import org.junit.Test;

public class UserTest {

    @Test
    public void testUserGettersAndSetters() {

        User user = new User();
        user.setUserId(1);
        user.setName("Ajay");
        user.setEmail("ajay@test.com");
        user.setPassword("1234");
        user.setRole("BUYER");

        assertEquals(1, user.getUserId());
        assertEquals("Ajay", user.getName());
        assertEquals("ajay@test.com", user.getEmail());
        assertEquals("1234", user.getPassword());
        assertEquals("BUYER", user.getRole());
    }
}
