package Service;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import Dao.UserDAO;
import Exception.AuthException;
import Exception.InvalidCredentialsException;
import Exception.ValidationException;
import Model.User;
import Service.AuthService;

public class AuthServiceTest {

    private AuthService authService;
    private UserDAO userDAOMock;

    @Before
    public void setUp() {
        userDAOMock = Mockito.mock(UserDAO.class);
        authService = new AuthService(userDAOMock);
    }
    
    @Test
    public void testRegisterSuccess() {

        User user = new User(
                "Ajay",
                "ajay@gmail.com",
                "pass123",
                "BUYER"
        );
        user.setPasswordHint("hint");

        // DAO behavior
        when(userDAOMock.registerUser(user)).thenReturn(true);

        boolean result = authService.register(user);

        assertTrue(result);

        // verify DAO call
        verify(userDAOMock, times(1)).registerUser(user);
    }
    
    @Test
    public void testLoginSuccess() {

        User mockUser = new User(
                "Ajay",
                "ajay@gmail.com",
                "pass123",
                "BUYER"
        );

        when(userDAOMock.loginUser("ajay@gmail.com", "pass123"))
                .thenReturn(mockUser);

        User user = authService.login("ajay@gmail.com", "pass123");

        assertNotNull(user);
        assertEquals("Ajay", user.getName());
    }


}