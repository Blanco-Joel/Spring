package com.example.library.infrastructure.persistence.repository;

import com.example.library.infrastructure.persistence.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("""
            SELECT DISTINCT c FROM CategoryEntity c
            LEFT JOIN FETCH c.book b
            WHERE (:name IS NULL OR c.name = :name)
           """)
    List<CategoryEntity> findByFilter(String name);

    @Override
    @Query("""
            SELECT DISTINCT c FROM CategoryEntity c
            LEFT JOIN FETCH c.book b
           """)
    List<CategoryEntity> findAll();
}