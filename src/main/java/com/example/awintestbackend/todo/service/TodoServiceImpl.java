package com.example.awintestbackend.todo.service;

import com.example.awintestbackend.todo.repository.TodoAdapter;
import com.example.awintestbackend.todo.repository.TodoRepositoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TodoServiceImpl implements TodoService {

    private final TodoAdapter todoAdapter;

    public TodoServiceImpl(TodoAdapter todoAdapter) {
        this.todoAdapter = todoAdapter;
    }

    @Override
    public TodoServiceDto createTodo(TodoServiceDto todo) {
        log.info("Creating todo for user: {}", todo.userid());
        TodoRepositoryDto repoDto = toRepoDto(todo);
        TodoRepositoryDto savedRepoDto = todoAdapter.save(repoDto);
        return toServiceDto(savedRepoDto);
    }

    @Override
    public Optional<TodoServiceDto> getTodoById(Long id) {
        log.info("Fetching todo with id: {}", id);
        return todoAdapter.findById(id).map(this::toServiceDto);
    }

    @Override
    public List<TodoServiceDto> getAllTodos() {
        log.info("Fetching all todos");
        return todoAdapter.findAll().stream()
                .map(this::toServiceDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoServiceDto> getTodosByUserid(Long userid) {
        log.info("Fetching todos for user: {}", userid);
        return todoAdapter.findByUserid(userid).stream()
                .map(this::toServiceDto)
                .collect(Collectors.toList());
    }

    @Override
    public TodoServiceDto updateTodo(Long id, TodoServiceDto todo) {
        log.info("Updating todo with id: {}", id);
        TodoRepositoryDto repoDto = new TodoRepositoryDto(id, todo.userid(), todo.description(), todo.icon(), todo.state());
        TodoRepositoryDto updatedRepoDto = todoAdapter.save(repoDto);
        return toServiceDto(updatedRepoDto);
    }

    @Override
    public void deleteTodo(Long id) {
        log.info("Deleting todo with id: {}", id);
        todoAdapter.deleteById(id);
    }

    @Override
    public void deleteTodosByUserid(Long userid) {
        log.info("Deleting todos for user: {}", userid);
        todoAdapter.deleteByUserid(userid);
    }

    private TodoRepositoryDto toRepoDto(TodoServiceDto dto) {
        return new TodoRepositoryDto(dto.id(), dto.userid(), dto.description(), dto.icon(), dto.state());
    }

    private TodoServiceDto toServiceDto(TodoRepositoryDto dto) {
        return new TodoServiceDto(dto.id(), dto.userid(), dto.description(), dto.icon(), dto.state());
    }
}
