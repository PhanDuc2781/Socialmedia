package com.example.socialmediaapp.Model;

public class ChatList {
    public String id , sender , reciver;

    public ChatList(){}

    public ChatList(String id, String sender, String reciver) {
        this.id = id;
        this.sender = sender;
        this.reciver = reciver;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }
}
