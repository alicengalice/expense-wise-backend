package com.expensewise.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.expensewise.dto.request.ExpenseCreateDTO;
import com.expensewise.dto.request.ExpenseUpdateDTO;
import com.expensewise.dto.response.CategoryResponseDTO;
import com.expensewise.dto.response.ExpenseResponseDTO;
import com.expensewise.dto.response.UserResponseDTO;
import com.expensewise.entity.Category;
import com.expensewise.entity.Expense;
import com.expensewise.entity.User;

/**
 * MapStruct mapper for Expense entity and its DTOs.
 * 
 * This interface is processed at compile-time to generate implementation code.
 * Spring component model makes it a Spring bean that can be injected.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ExpenseMapper {
    
    // ========== Entity to Response DTO ==========
    
    /**
     * Convert Expense entity to response DTO.
     * MapStruct automatically maps nested entities to nested DTOs.
     */
    ExpenseResponseDTO toResponseDTO(Expense expense);
    
    CategoryResponseDTO toCategoryResponseDTO(Category category);
    
    UserResponseDTO toUserResponseDTO(User user);
    
    
    // ========== Create DTO to Entity ==========
    
    /**
     * Convert create DTO to entity for persistence.
     * Note: categoryId and userId need to be handled in service layer
     * because they require database lookups.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)  // Set in service
    @Mapping(target = "user", ignore = true)      // Set in service
    Expense toEntity(ExpenseCreateDTO createDTO);
    
    
    // ========== Update existing Entity ==========
    
    /**
     * Update existing expense entity from update DTO.
     * Only non-null values from DTO will update the entity.
     * Relationships (category, user) are handled in service layer.
     * 
     * @param updateDTO The DTO with updated values
     * @param expense The existing entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)  // Set in service
    @Mapping(target = "user", ignore = true)      // Set in service
    void updateEntityFromDTO(ExpenseUpdateDTO updateDTO, @MappingTarget Expense expense);
}
