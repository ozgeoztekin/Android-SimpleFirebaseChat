package com.oztekino.simplefirebasechat;

public class Message {

    private String text;
    private String senderName;
    private String senderId;
    private int colorCode;

    public Message() {
    }

    public Message(String text, String senderName, String senderId, int colorCode) {
        this.text = text;
        this.senderName = senderName;
        this.senderId = senderId;
        this.colorCode = colorCode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }
}