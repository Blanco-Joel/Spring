package com.example.library.domain.utils.mappers;

import com.example.library.domain.model.Category;
import com.example.library.infrastructure.persistence.entities.CategoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toDomain(CategoryEntity entity);

    List<Category> toDomainList(List<CategoryEntity> entities);
}