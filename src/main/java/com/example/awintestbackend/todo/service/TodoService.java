package com.example.awintestbackend.todo.service;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    TodoServiceDto createTodo(TodoServiceDto todo);
    Optional<TodoServiceDto> getTodoById(Long id);
    List<TodoServiceDto> getAllTodos();
    List<TodoServiceDto> getTodosByUserid(Long userid);
    TodoServiceDto updateTodo(Long id, TodoServiceDto todo);
    void deleteTodo(Long id);
    void deleteTodosByUserid(Long userid);
}
