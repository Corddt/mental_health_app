package com.corddt.mental_health_app;

public class Plan {
    private int id;
    private String content;
    private boolean isCompleted;
    private String timestamp;

    public Plan(int id, String content, boolean isCompleted) {
        this.id = id;
        this.content = content;
        this.isCompleted = isCompleted;
    }

    // Getters
    public int getId() { return id; }
    public String getContent() { return content; }
    public boolean isCompleted() { return isCompleted; }

    // Setters
    public void setContent(String content) { this.content = content; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}
