package com.example.company;
public class Message {
    private String text;
    private String nickname;
    private String uid;

    public Message(String messageText, String currentUserNickname, String currentUserId) {
        // Default constructor required for calls to DataSnapshot.getValue(Message.class)
    }

    public Message(String text, String nickname) {
        this.text = text;
        this.nickname = nickname;
    }

    public String getText() {
        return text;
    }

    public String getNickname() {
        return nickname;
    }

    public void setText(String text) {
        this.text = text;
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
}
