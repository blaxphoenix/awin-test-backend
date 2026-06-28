package com.example.awintestbackend.todo.controller;

import com.example.awintestbackend.todo.service.TodoService;
import com.example.awintestbackend.todo.service.TodoServiceDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/u2m/v1/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<TodoControllerDto> createTodo(@Valid @RequestBody TodoControllerDto todoDto) {
        TodoServiceDto serviceDto = toServiceDto(todoDto);
        TodoServiceDto createdTodo = todoService.createTodo(serviceDto);
        return new ResponseEntity<>(toControllerDto(createdTodo), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoControllerDto> getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id)
                .map(this::toControllerDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<TodoControllerDto> getAllTodos(@RequestParam(required = false) Long userid) {
        List<TodoServiceDto> todos;
        if (userid != null) {
            todos = todoService.getTodosByUserid(userid);
        } else {
            todos = todoService.getAllTodos();
        }
        return todos.stream()
                .map(this::toControllerDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoControllerDto> updateTodo(@PathVariable Long id, @Valid @RequestBody TodoControllerDto todoDto) {
        TodoServiceDto serviceDto = toServiceDto(todoDto);
        TodoServiceDto updatedTodo = todoService.updateTodo(id, serviceDto);
        return ResponseEntity.ok(toControllerDto(updatedTodo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoControllerDto> toggleTodoState(@PathVariable Long id) {
        return todoService.toggleTodoState(id)
                .map(this::toControllerDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private TodoServiceDto toServiceDto(TodoControllerDto dto) {
        return new TodoServiceDto(dto.id(), dto.userid(), dto.description(), dto.icon(), dto.state());
    }

    private TodoControllerDto toControllerDto(TodoServiceDto dto) {
        return new TodoControllerDto(dto.id(), dto.userid(), dto.description(), dto.icon(), dto.state());
    }
}
