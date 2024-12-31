package com.troy2ooo.petdiary;

public class HealthRecord {
    private String date;
    private String content;

    public HealthRecord(String date, String content) {
        this.date = date;
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
} 