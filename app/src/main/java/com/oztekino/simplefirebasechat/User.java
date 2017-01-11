package com.oztekino.simplefirebasechat;

public class User {

    private String userId;
    private String username;
    private int status;
    private boolean isTyping;
    private int colorCode;

    public User() {
    }

    public User(String userId, String username, int status, boolean isTyping, int colorCode) {
        this.userId = userId;
        this.username = username;
        this.status = status;
        this.isTyping = isTyping;
        this.colorCode = colorCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }
}
