package com.devops.app.todo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public List<TodoEntity> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public TodoEntity get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoEntity create(@Valid @RequestBody TodoCreateRequest req) {
        return service.create(req.getTitle());
    }

    @PutMapping("/{id}")
    public TodoEntity update(@PathVariable Long id, @Valid @RequestBody TodoUpdateRequest req) {
        return service.update(id, req.getTitle(), req.isCompleted());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
