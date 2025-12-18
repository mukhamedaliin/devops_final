package com.devops.app.todo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TodoUpdateRequest {
    @NotBlank(message = "title must not be blank")
    @Size(max = 200, message = "title must be <= 200 chars")
    private String title;

    private boolean completed;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
