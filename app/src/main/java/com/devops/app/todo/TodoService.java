package com.devops.app.todo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
@Service
public class TodoService {
    private final TodoRepository repo;
    public TodoService(TodoRepository repo) {
        this.repo = repo;
    }

    public List<TodoEntity> list() {
        return repo.findAll();
    }

    public TodoEntity get(Long id) {
        return repo.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
    }

    @Transactional
    public TodoEntity create(String title) {
        TodoEntity e = new TodoEntity(title);
        return repo.save(e);
    }

    @Transactional
    public TodoEntity update(Long id, String title, boolean completed) {
        TodoEntity e = get(id);
        e.setTitle(title);
        e.setCompleted(completed);
        e.setUpdatedAt(Instant.now());
        return repo.save(e);
    }

    @Transactional
    public void delete(Long id) {
        TodoEntity e = get(id);
        repo.delete(e);
    }
}
