package com.example.awintestbackend.todo.service;

import com.example.awintestbackend.todo.repository.TodoAdapter;
import com.example.awintestbackend.todo.repository.TodoRepositoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoServiceImpl implements TodoService {

    private final TodoAdapter todoAdapter;

    public TodoServiceImpl(TodoAdapter todoAdapter) {
        this.todoAdapter = todoAdapter;
    }

    @Override
    public TodoData createTodo(TodoData todo) {
        log.info("Creating todo for user: {}", todo.userid());
        TodoRepositoryDto repoDto = toRepoDto(todo);
        TodoRepositoryDto savedRepoDto = todoAdapter.save(repoDto);
        return toServiceDto(savedRepoDto);
    }

    @Override
    public Optional<TodoData> getTodoById(Long id) {
        log.info("Fetching todo with id: {}", id);
        return todoAdapter.findById(id).map(this::toServiceDto);
    }

    @Override
    public List<TodoData> getAllTodos() {
        log.info("Fetching all todos");
        return todoAdapter.findAll().stream()
                .map(this::toServiceDto)
                .toList();
    }

    @Override
    public List<TodoData> getTodosByUserid(Long userid) {
        log.info("Fetching todos for user: {}", userid);
        return todoAdapter.findByUserid(userid).stream()
                .map(this::toServiceDto)
                .toList();
    }

    @Override
    public TodoData updateTodo(Long id, TodoData todo) {
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

    @Override
    public Optional<TodoData> toggleTodoState(Long id) {
        log.info("Toggling state for todo with id: {}", id);
        return todoAdapter.findById(id).map(todo -> {
            TodoRepositoryDto updatedDto = new TodoRepositoryDto(
                    todo.id(),
                    todo.userid(),
                    todo.description(),
                    todo.icon(),
                    !todo.state()
            );
            return toServiceDto(todoAdapter.save(updatedDto));
        });
    }

    private TodoRepositoryDto toRepoDto(TodoData dto) {
        return new TodoRepositoryDto(dto.id(), dto.userid(), dto.description(), dto.icon(), dto.state());
    }

    private TodoData toServiceDto(TodoRepositoryDto dto) {
        return new TodoData(dto.id(), dto.userid(), dto.description(), dto.icon(), dto.state());
    }
}
