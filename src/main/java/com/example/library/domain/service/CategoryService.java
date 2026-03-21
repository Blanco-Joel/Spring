package com.example.library.domain.service;

import com.example.library.application.model.exceptions.CategoryNotFound;
import com.example.library.application.model.exceptions.LibraryException;
import com.example.library.domain.model.Category;
import com.example.library.domain.utils.mappers.CategoryMapper;
import com.example.library.infrastructure.persistence.entities.CategoryEntity;
import com.example.library.infrastructure.persistence.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<Category> findAll() {
        try {
            return categoryMapper.toDomainList(categoryRepository.findAll());
        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }

    public Category create(String name) {
        try {
            CategoryEntity entity = new CategoryEntity();
            entity.setName(name);

            return categoryMapper.toDomain(categoryRepository.save(entity));
        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }

    public Category findById(Long id) {
        try {
            Optional<CategoryEntity> entity = categoryRepository.findById(id);

            if (entity.isEmpty()) {
                throw new CategoryNotFound("Category not found");
            }

            return categoryMapper.toDomain(entity.get());

        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }

    public Category update(Long id, String name) {
        try {
            CategoryEntity entity = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            updateIfNotBlank(name, entity::setName);

            return categoryMapper.toDomain(categoryRepository.save(entity));

        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }

    public void delete(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }

    public List<Category> findByFilter(String name) {
        try {
            List<CategoryEntity> entities = categoryRepository.findByFilter(name);

            if (entities.isEmpty()) {
                throw new CategoryNotFound("Category not found with filters");
            }

            return categoryMapper.toDomainList(entities);

        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }

    public static void updateIfNotBlank(String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isBlank()) {
            setter.accept(newValue);
        }
    }
}