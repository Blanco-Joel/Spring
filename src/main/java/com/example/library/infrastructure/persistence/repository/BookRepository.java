package com.example.library.infrastructure.persistence.repository;

import com.example.library.infrastructure.persistence.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity,Long > {

    List<Optional<BookEntity>> findByAuthor(String author);
}
