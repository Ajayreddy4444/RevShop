package Model;

/**
 * Represents an in-app notification
 */
public class Notification {

    // Notification primary key
    private int notificationId;

    // User who receives notification
    private int userId;

    // Notification message
    private String message;

    // Created timestamp
    private String createdAt;

    // Get notificationId
    public int getNotificationId() {
        return notificationId;
    }

    // Set notificationId
    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    // Get userId
    public int getUserId() {
        return userId;
    }

    // Set userId
    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Get message
    public String getMessage() {
        return message;
    }

    // Set message
    public void setMessage(String message) {
        this.message = message;
    }

    // Get createdAt
    public String getCreatedAt() {
        return createdAt;
    }

    // Set createdAt
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
