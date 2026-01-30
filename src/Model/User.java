package Model;

/**
 * Represents a system user (Buyer or Seller)
 */
public class User {

    // User primary key
    private int userId;

    // User basic details
    private String name;
    private String email;
    private String password;
    private String role; // BUYER or SELLER

    // Password recovery hint
    private String passwordHint;

    // Default constructor
    public User() {
    }

    // Constructor used during registration
    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Constructor with userId
    public User(int userId, String name, String email, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Get userId
    public int getUserId() {
        return userId;
    }

    // Set userId
    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Get name
    public String getName() {
        return name;
    }

    // Set name
    public void setName(String name) {
        this.name = name;
    }

    // Get email
    public String getEmail() {
        return email;
    }

    // Set email
    public void setEmail(String email) {
        this.email = email;
    }

    // Get password
    public String getPassword() {
        return password;
    }

    // Set password
    public void setPassword(String password) {
        this.password = password;
    }

    // Get role
    public String getRole() {
        return role;
    }

    // Set role
    public void setRole(String role) {
        this.role = role;
    }

    // Get password hint
    public String getPasswordHint() {
        return passwordHint;
    }

    // Set password hint
    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }
}
