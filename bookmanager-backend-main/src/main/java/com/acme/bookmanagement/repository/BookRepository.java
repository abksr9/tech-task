package com.acme.bookmanagement.repository;

import com.acme.bookmanagement.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByPublishedDateBetween(LocalDate start, LocalDate end);
    Optional<Book> findByTitle(String title);
}

