package com.expensewise.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.expensewise.dto.request.CategoryCreateDTO;
import com.expensewise.dto.response.CategoryResponseDTO;
import com.expensewise.entity.Category;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)

public interface CategoryMapper {
    CategoryResponseDTO toCategoryResponseDTO(Category category);
    
    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryCreateDTO createDTO);
}
