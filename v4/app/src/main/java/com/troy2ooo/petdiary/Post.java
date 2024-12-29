package com.troy2ooo.petdiary;

public class Post {
    private String userID;
    private String content;
    private long timestamp;
    private String imageUrl; // 新增圖片 URL 欄位

    public Post(String userID, String content, long timestamp, String imageUrl) {
        this.userID = userID;
        this.content = content;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }
}
