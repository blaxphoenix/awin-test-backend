package com.example.awintestbackend.todo.repository;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TodoAdapter {

    private final TodoRepository todoRepository;

    public TodoAdapter(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoRepositoryDto save(TodoRepositoryDto dto) {
        TodoEntity entity = toEntity(dto);
        TodoEntity savedEntity = todoRepository.save(entity);
        return toDto(savedEntity);
    }

    public Optional<TodoRepositoryDto> findById(Long id) {
        return todoRepository.findById(id).map(this::toDto);
    }

    public List<TodoRepositoryDto> findAll() {
        return todoRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TodoRepositoryDto> findByUserid(Long userid) {
        return todoRepository.findByUserid(userid).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        todoRepository.deleteById(id);
    }

    public void deleteByUserid(Long userid) {
        todoRepository.deleteByUserid(userid);
    }

    private TodoEntity toEntity(TodoRepositoryDto dto) {
        return new TodoEntity(dto.id(), dto.userid(), dto.description(), dto.icon(), dto.state());
    }

    private TodoRepositoryDto toDto(TodoEntity entity) {
        return new TodoRepositoryDto(entity.getId(), entity.getUserid(), entity.getDescription(), entity.getIcon(), entity.isState());
    }
}
