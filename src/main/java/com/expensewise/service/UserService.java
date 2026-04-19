package com.expensewise.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.expensewise.dto.mapper.UserMapper;
import com.expensewise.dto.request.UserCreateDTO;
import com.expensewise.dto.response.UserResponseDTO;
import com.expensewise.entity.User;
import com.expensewise.repository.UserRepository;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserService(UserRepository userRepository, UserMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(mapper::toUserResponseDTO)
            .toList();
    }

    public Page<UserResponseDTO> getAllPaginated(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        return page.map(mapper::toUserResponseDTO);
    }

    public UserResponseDTO getById(Long id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid user ID requested: {}", id);
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.error("User not found with ID: {}", id);
            return new RuntimeException("User not found: " + id);
        });
        return mapper.toUserResponseDTO(user);
    }

    public UserResponseDTO create(UserCreateDTO createDTO) {
        if (createDTO == null) {
            logger.warn("Attempt to create null user");
            throw new IllegalArgumentException("User cannot be null");
        }
        if (createDTO.username() == null || createDTO.username().isBlank()) {
            logger.warn("User username is empty");
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (createDTO.email() == null || createDTO.email().isBlank()) {
            logger.warn("User email is empty");
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (userRepository.existsByUsername(createDTO.username())) {
            logger.warn("Attempt to create user with existing username: {}", createDTO.username());
            throw new IllegalArgumentException("Username already exists");
        }

        // Map DTO to entity
        User user = mapper.toEntity(createDTO);

        logger.info("Creating new user: {}", user.getUsername());
        User saved = userRepository.save(user);
        return mapper.toUserResponseDTO(saved);
    }

    public UserResponseDTO update(Long id, UserCreateDTO updateDTO) {
        if (id == null || id <= 0) {
            logger.warn("Invalid user ID for update: {}", id);
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        if (updateDTO == null) {
            logger.warn("Attempt to update with null user");
            throw new IllegalArgumentException("Updated user cannot be null");
        }
        
        // Fetch existing user
        User existing = userRepository.findById(id).orElseThrow(() -> {
            logger.error("User not found with ID: {}", id);
            return new RuntimeException("User not found: " + id);
        });
        
        // Update fields
        if (updateDTO.username() != null && !updateDTO.username().isBlank()) {
            if (!existing.getUsername().equals(updateDTO.username()) && userRepository.existsByUsername(updateDTO.username())) {
                logger.warn("Attempt to update user with existing username: {}", updateDTO.username());
                throw new IllegalArgumentException("Username already exists");
            }
            existing.setUsername(updateDTO.username());
        }
        if (updateDTO.email() != null && !updateDTO.email().isBlank()) {
            existing.setEmail(updateDTO.email());
        }
        
        logger.info("Updated user with ID: {}", id);
        User save = userRepository.save(existing);
        return mapper.toUserResponseDTO(save);
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid user ID for deletion: {}", id);
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        if (!userRepository.existsById(id)) {
            logger.error("Attempt to delete non-existent user with ID: {}", id);
            throw new RuntimeException("User not found: " + id);
        }
        logger.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
    }
}