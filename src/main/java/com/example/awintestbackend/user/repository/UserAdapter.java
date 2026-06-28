package com.example.awintestbackend.user.repository;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserAdapter {

    private final UserRepository userRepository;

    public UserAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRepositoryDto save(UserRepositoryDto userRepositoryDto) {
        UserEntity entity = toEntity(userRepositoryDto);
        UserEntity savedEntity = userRepository.save(entity);
        return toDto(savedEntity);
    }

    public Optional<UserRepositoryDto> findById(Long id) {
        return userRepository.findById(id).map(this::toDto);
    }

    public List<UserRepositoryDto> findAll() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private UserEntity toEntity(UserRepositoryDto dto) {
        return new UserEntity(dto.userid(), dto.name(), dto.email(), dto.currency());
    }

    private UserRepositoryDto toDto(UserEntity entity) {
        return new UserRepositoryDto(entity.getUserid(), entity.getName(), entity.getEmail(), entity.getCurrency());
    }
}
