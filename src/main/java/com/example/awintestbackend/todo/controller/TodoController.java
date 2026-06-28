package com.example.awintestbackend.todo.controller;

import com.example.awintestbackend.exception.ResourceNotFoundException;
import com.example.awintestbackend.todo.service.TodoData;
import com.example.awintestbackend.todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/u2m/v{version}/todos", version = "1")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<TodoControllerDto> createTodo(@Valid @RequestBody TodoControllerDto todoDto) {
        TodoData serviceDto = toServiceDto(todoDto);
        TodoData createdTodo = todoService.createTodo(serviceDto);
        return new ResponseEntity<>(toControllerDto(createdTodo), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoControllerDto> getTodoById(@PathVariable Long id) {
        TodoControllerDto todo = todoService.getTodoById(id)
                .map(this::toControllerDto)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
        return ResponseEntity.ok(todo);
    }

    @GetMapping
    public ResponseEntity<List<TodoControllerDto>> getAllTodos(@RequestParam(required = false) Long userid) {
        List<TodoData> todos;
        if (userid != null) {
            todos = todoService.getTodosByUserid(userid);
        } else {
            todos = todoService.getAllTodos();
        }
        List<TodoControllerDto> result = todos.stream()
                .map(this::toControllerDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoControllerDto> updateTodo(@PathVariable Long id, @Valid @RequestBody TodoControllerDto todoDto) {
        TodoData serviceDto = toServiceDto(todoDto);
        TodoData updatedTodo = todoService.updateTodo(id, serviceDto);
        return ResponseEntity.ok(toControllerDto(updatedTodo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoControllerDto> toggleTodoState(@PathVariable Long id) {
        TodoControllerDto todo = todoService.toggleTodoState(id)
                .map(this::toControllerDto)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
        return ResponseEntity.ok(todo);
    }

    private TodoData toServiceDto(TodoControllerDto dto) {
        return new TodoData(dto.id(), dto.userid(), dto.description(), dto.icon(), dto.state());
    }

    private TodoControllerDto toControllerDto(TodoData dto) {
        return new TodoControllerDto(dto.id(), dto.userid(), dto.description(), dto.icon(), dto.state());
    }
}
