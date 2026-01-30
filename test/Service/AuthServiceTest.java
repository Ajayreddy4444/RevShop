package Service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Model.User;
import Exception.AuthException;
import Exception.InvalidCredentialsException;
import Exception.ValidationException;

public class AuthServiceTest {

    private AuthService authService;

    // Runs before every test case
    @Before
    public void setUp() {
        authService = new AuthService();
    }

    // ================= REGISTER TESTS =================

    // ❌ Empty email should throw ValidationException
    @Test(expected = ValidationException.class)
    public void testRegisterWithEmptyEmail() {
        User user = new User("Ajay", "", "1234", "BUYER");
        user.setPasswordHint("pet");

        authService.register(user);
    }

    // ❌ Short password should throw ValidationException
    @Test(expected = ValidationException.class)
    public void testRegisterWithShortPassword() {
        User user = new User("Ajay", "ajay@test.com", "12", "BUYER");
        user.setPasswordHint("pet");

        authService.register(user);
    }

    // ❌ Missing password hint should throw ValidationException
    @Test(expected = ValidationException.class)
    public void testRegisterWithoutPasswordHint() {
        User user = new User("Ajay", "ajay@test.com", "1234", "BUYER");
        user.setPasswordHint("");

        authService.register(user);
    }

    // ================= LOGIN TESTS =================

    // ❌ Empty email during login
    @Test(expected = ValidationException.class)
    public void testLoginWithEmptyEmail() {
        authService.login("", "1234");
    }

    // ❌ Empty password during login
    @Test(expected = ValidationException.class)
    public void testLoginWithEmptyPassword() {
        authService.login("ajay@test.com", "");
    }

    // ❌ Invalid credentials
    @Test(expected = InvalidCredentialsException.class)
    public void testLoginWithInvalidCredentials() {
        authService.login("wrong@test.com", "wrongpass");
    }

    // ================= CHANGE PASSWORD TESTS =================

    // ❌ New password too short
    @Test(expected = ValidationException.class)
    public void testChangePasswordWithShortNewPassword() {
        authService.changePassword(1, "oldpass", "12");
    }

    // ❌ Wrong old password
    @Test(expected = InvalidCredentialsException.class)
    public void testChangePasswordWithWrongOldPassword() {
        authService.changePassword(1, "wrongOld", "newpass123");
    }

    // ================= FORGOT PASSWORD TESTS =================

    // ❌ Empty email
    @Test(expected = ValidationException.class)
    public void testForgotPasswordWithEmptyEmail() {
        authService.forgotPassword("", "pet", "newpass");
    }

    // ❌ Empty hint
    @Test(expected = ValidationException.class)
    public void testForgotPasswordWithEmptyHint() {
        authService.forgotPassword("ajay@test.com", "", "newpass");
    }

    // ❌ Invalid hint/email combination
    @Test(expected = AuthException.class)
    public void testForgotPasswordWithInvalidDetails() {
        authService.forgotPassword("ajay@test.com", "wrongHint", "newpass");
    }
}
