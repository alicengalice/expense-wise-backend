package com.expensewise.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.expensewise.dto.request.UserCreateDTO;
import com.expensewise.dto.response.UserResponseDTO;
import com.expensewise.entity.User;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    UserResponseDTO toUserResponseDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)  // Auto-generated
    @Mapping(target = "expenses", ignore = true)   // Set by relationship
    User toEntity(UserCreateDTO createDTO);
} 
