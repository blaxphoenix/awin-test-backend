package com.example.awintestbackend.todo.repository;
import com.example.awintestbackend.todo.TodoMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TodoAdapter {
    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    public TodoAdapter(TodoRepository todoRepository, TodoMapper todoMapper) {
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
    }

    public TodoRepositoryDto save(TodoRepositoryDto dto) {
        TodoEntity entity = todoMapper.toEntity(dto);
        TodoEntity savedEntity = todoRepository.save(entity);
        return todoMapper.toRepoDto(savedEntity);
    }

    public Optional<TodoRepositoryDto> findById(Long id) {
        return todoRepository.findById(id).map(todoMapper::toRepoDto);
    }

    public List<TodoRepositoryDto> findAll() {
        return todoRepository.findAll().stream()
                .map(todoMapper::toRepoDto)
                .toList();
    }

    public List<TodoRepositoryDto> findByUserid(Long userid) {
        return todoRepository.findByUserid(userid).stream()
                .map(todoMapper::toRepoDto)
                .toList();
    }

    public void deleteById(Long id) {
        todoRepository.deleteById(id);
    }

    public void deleteByUserid(Long userid) {
        todoRepository.deleteByUserid(userid);
    }
}
