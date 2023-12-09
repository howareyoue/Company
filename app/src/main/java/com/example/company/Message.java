package com.example.company;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private String text;
    private String nickname;
    private String uid;
    private long timestamp; // Add this field to store the timestamp

    public Message() {}

    public Message(String text, String nickname, String uid) {
        this.text = text;
        this.nickname = nickname;
        this.uid = uid;
        this.timestamp = System.currentTimeMillis(); // Initialize the timestamp when the message is created
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    // Getters and setters for the new timestamp field
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Convert to Map for Firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("text", text);
        result.put("nickname", nickname);
        result.put("uid", uid);
        result.put("timestamp", timestamp);
        return result;
    }
}
