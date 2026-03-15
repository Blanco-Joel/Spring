package com.example.library.infrastructure.persistence.repository;

import com.example.library.infrastructure.persistence.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity,Long > {
    @Query("""
                SELECT DISTINCT b FROM BookEntity b
                JOIN FETCH b.bookCategories bc
                WHERE (:title IS NULL OR b.title = :title)
                AND (:author IS NULL OR b.author = :author)
                AND (:year IS NULL OR b.publishedYear = :year)
                """)
    List<BookEntity> findByFilter(String title,String author,String year);
    @Override
    @Query("SELECT DISTINCT b FROM BookEntity b JOIN FETCH b.bookCategories bc")
    List<BookEntity> findAll();

    @Override
    @Query("SELECT DISTINCT b FROM BookEntity b JOIN FETCH b.bookCategories bc WHERE b.book_id = :id")
    Optional<BookEntity> findById(Long id);
}
