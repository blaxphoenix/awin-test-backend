package com.example.awintestbackend.todo;

import com.example.awintestbackend.todo.controller.TodoControllerDto;
import com.example.awintestbackend.todo.repository.TodoEntity;
import com.example.awintestbackend.todo.repository.TodoRepositoryDto;
import com.example.awintestbackend.todo.service.TodoData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TodoMapper {

    // Controller <-> Service
    TodoData toData(TodoControllerDto dto);
    TodoControllerDto toControllerDto(TodoData data);

    // Service <-> Repository
    TodoRepositoryDto toRepoDto(TodoData data);
    TodoData toData(TodoRepositoryDto dto);

    @Mapping(target = "id", source = "id")
    TodoRepositoryDto toRepoDto(Long id, TodoData data);

    @Mapping(target = "state", source = "state")
    TodoRepositoryDto withState(TodoRepositoryDto dto, boolean state);

    // Repository <-> Entity
    TodoEntity toEntity(TodoRepositoryDto dto);
    TodoRepositoryDto toRepoDto(TodoEntity entity);
}
