package com.example.library.infrastructure.persistence.repository;

import com.example.library.infrastructure.persistence.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity,Long > {


}
