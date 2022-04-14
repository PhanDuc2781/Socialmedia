package com.example.socialmediaapp.Model;

public class Chat {
    private String id , senderId ,message , recipient , timeSend , emoji , isseen;

    public Chat(){}

    public Chat(String id, String senderId, String message, String recipient, String timeSend, String emoji, String isseen) {
        this.id = id;
        this.senderId = senderId;
        this.message = message;
        this.recipient = recipient;
        this.timeSend = timeSend;
        this.emoji = emoji;
        this.isseen = isseen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getTimeSend() {
        return timeSend;
    }

    public void setTimeSend(String timeSend) {
        this.timeSend = timeSend;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getIsseen() {
        return isseen;
    }

    public void setIsseen(String isseen) {
        this.isseen = isseen;
    }
}
