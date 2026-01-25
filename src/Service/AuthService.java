package Service;

import Dao.UserDAO;
import Model.User;

public class AuthService {

    private UserDAO userDAO = new UserDAO();

    public boolean register(User user) {
        return userDAO.registerUser(user);
    }

    public User login(String email, String password) {
        return userDAO.loginUser(email, password);
    }
}
