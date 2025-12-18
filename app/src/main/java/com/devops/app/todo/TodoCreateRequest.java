package com.devops.app.todo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TodoCreateRequest {
    @NotBlank(message = "title must not be blank")
    @Size(max = 200, message = "title must be <= 200 chars")
    private String title;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
