package com.example.company;
public class Message {
    private String text;
    private String nickname;
    private String uid;

    public Message() {}

    public Message(String text, String nickname, String currentUserId) {
        this.text = text;
        this.nickname = nickname;
        this.uid = currentUserId; // 이 부분을 수정
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
