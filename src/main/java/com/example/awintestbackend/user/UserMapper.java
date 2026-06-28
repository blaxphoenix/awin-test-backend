package com.example.awintestbackend.user;

import com.example.awintestbackend.user.controller.UserControllerDto;
import com.example.awintestbackend.user.repository.UserRepositoryDto;
import com.example.awintestbackend.user.repository.UserEntity;
import com.example.awintestbackend.user.service.UserData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    // Controller <-> Service
    UserData toData(UserControllerDto dto);
    UserControllerDto toControllerDto(UserData data);

    // Service <-> Repository
    UserRepositoryDto toRepoDto(UserData data);
    UserData toData(UserRepositoryDto dto);

    @Mapping(target = "userid", source = "userid")
    UserRepositoryDto toRepoDto(Long userid, UserData data);

    // Repository <-> Entity
    UserEntity toEntity(UserRepositoryDto dto);
    UserRepositoryDto toRepoDto(UserEntity entity);
}
