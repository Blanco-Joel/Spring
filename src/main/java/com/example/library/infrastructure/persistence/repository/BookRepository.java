package com.example.library.infrastructure.persistence.repository;

import com.example.library.infrastructure.persistence.entities.BookEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity,Long > {
    @Query("""
                SELECT b FROM BookEntity b
                WHERE (:title IS NULL OR b.title = :title)
                AND (:author IS NULL OR b.author = :author)
                AND (:year IS NULL OR b.publishedYear = :year)
                """)
    List<Optional<BookEntity>>findByFilter(String title,String author,String year);

    @Override
    @EntityGraph(attributePaths = {"loans", "bookCategories"})
    List<BookEntity> findAll();

}
