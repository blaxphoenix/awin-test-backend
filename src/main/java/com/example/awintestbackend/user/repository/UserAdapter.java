package com.example.awintestbackend.user.repository;
import com.example.awintestbackend.user.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserAdapter {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserAdapter(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserRepositoryDto save(UserRepositoryDto userRepositoryDto) {
        UserEntity entity = userMapper.toEntity(userRepositoryDto);
        UserEntity savedEntity = userRepository.save(entity);
        return userMapper.toRepoDto(savedEntity);
    }

    public Optional<UserRepositoryDto> findById(Long id) {
        return userRepository.findById(id).map(userMapper::toRepoDto);
    }

    public List<UserRepositoryDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toRepoDto)
                .toList();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
