package com.example.awintestbackend.todo.service;

import com.example.awintestbackend.todo.TodoMapper;
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
    private final TodoMapper todoMapper;

    public TodoServiceImpl(TodoAdapter todoAdapter, TodoMapper todoMapper) {
        this.todoAdapter = todoAdapter;
        this.todoMapper = todoMapper;
    }

    @Override
    public TodoData createTodo(TodoData todo) {
        log.info("Creating todo for user: {}", todo.userid());
        TodoRepositoryDto repoDto = todoMapper.toRepoDto(todo);
        TodoRepositoryDto savedRepoDto = todoAdapter.save(repoDto);
        return todoMapper.toData(savedRepoDto);
    }

    @Override
    public Optional<TodoData> getTodoById(Long id) {
        log.info("Fetching todo with id: {}", id);
        return todoAdapter.findById(id).map(todoMapper::toData);
    }

    @Override
    public List<TodoData> getAllTodos() {
        log.info("Fetching all todos");
        return todoAdapter.findAll().stream()
                .map(todoMapper::toData)
                .toList();
    }

    @Override
    public List<TodoData> getTodosByUserid(Long userid) {
        log.info("Fetching todos for user: {}", userid);
        return todoAdapter.findByUserid(userid).stream()
                .map(todoMapper::toData)
                .toList();
    }

    @Override
    public TodoData updateTodo(Long id, TodoData todo) {
        log.info("Updating todo with id: {}", id);
        TodoRepositoryDto repoDto = todoMapper.toRepoDto(id, todo);
        TodoRepositoryDto updatedRepoDto = todoAdapter.save(repoDto);
        return todoMapper.toData(updatedRepoDto);
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
            TodoRepositoryDto updatedDto = todoMapper.withState(todo, !todo.state());
            return todoMapper.toData(todoAdapter.save(updatedDto));
        });
    }
}
