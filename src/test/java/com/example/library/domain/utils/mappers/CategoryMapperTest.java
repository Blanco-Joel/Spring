package com.example.library.domain.utils.mappers;

import com.example.library.domain.model.Category;
import com.example.library.infrastructure.persistence.entities.CategoryEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryMapperTest {

    private final CategoryMapper mapper = new CategoryMapperImpl();

    @Test
    void toDomainMapsCategoryFields() {
        CategoryEntity entity = new CategoryEntity(1L, "Programming", null);

        Category category = mapper.toDomain(entity);

        assertThat(category.getName()).isEqualTo("Programming");
    }

    @Test
    void toDomainListMapsAllItems() {
        List<Category> categories = mapper.toDomainList(List.of(
                new CategoryEntity(1L, "Programming", null),
                new CategoryEntity(2L, "Architecture", null)
        ));

        assertThat(categories).extracting(Category::getName).containsExactly("Programming", "Architecture");
    }
}
