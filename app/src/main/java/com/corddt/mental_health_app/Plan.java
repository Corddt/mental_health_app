package com.corddt.mental_health_app;

public class Plan {
    private int id;
    private String content;
    private boolean isCompleted;

    public Plan(int id, String content, boolean isCompleted) {
        this.id = id;
        this.content = content;
        this.isCompleted = isCompleted;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getContent() { return content; }
    public boolean isCompleted() { return isCompleted; }
    public void setContent(String content) { this.content = content; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}
