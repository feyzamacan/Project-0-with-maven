package models;

import java.util.Date;

public class Action {
    private int actionId;
    private int userId;
    private float amount;
    private Date timestamp;
    private String description; // from action_type

    private String username;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Action() {}

    public int getActionId() { return actionId; }
    public void setActionId(int actionId) { this.actionId = actionId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public float getAmount() { return amount; }
    public void setAmount(float amount) { this.amount = amount; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
