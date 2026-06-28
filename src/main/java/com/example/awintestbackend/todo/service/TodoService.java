package com.example.awintestbackend.todo.service;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    TodoData createTodo(TodoData todo);

    Optional<TodoData> getTodoById(Long id);

    List<TodoData> getAllTodos();

    List<TodoData> getTodosByUserid(Long userid);

    TodoData updateTodo(Long id, TodoData todo);

    void deleteTodo(Long id);

    void deleteTodosByUserid(Long userid);

    Optional<TodoData> toggleTodoState(Long id);
}
