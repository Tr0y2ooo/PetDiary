package com.troy2ooo.petdiary;

public class Post {
    private String userID;
    private String content;
    private long timestamp;

    public Post(String userID, String content, long timestamp) {
        this.userID = userID;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getUserID() {
        return userID;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
