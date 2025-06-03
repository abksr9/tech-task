package com.acme.bookmanagement.service;

import com.acme.bookmanagement.model.Author;
import com.acme.bookmanagement.model.Book;
import com.acme.bookmanagement.repository.AuthorRepository;
import com.acme.bookmanagement.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BookService.class);

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book saveBook(Book book) {
        if (book.getTitle() != null) {
            // Check if a book with the same title already exists
            Optional<Book> existingBook = bookRepository.findByTitle(book.getTitle());
            if (existingBook.isPresent()) {
                logger.info("Book with title '{}' already exists, reusing existing book.", book.getTitle());
                return existingBook.get();
            }
        }
        Author author = book.getAuthor();
        if (author != null) {
            // Reuse author if already exists
            Author existingAuthor = authorRepository.findByName(author.getName())
                    .orElseGet(() -> authorRepository.save(author));
            book.setAuthor(existingAuthor);
        }
        return bookRepository.save(book);
    }

    public List<Book> findBooksByDateRange(String startDate, String endDate) {
        List<Book> books = bookRepository.findByPublishedDateBetween(
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)
        );
        return books != null ? books : new ArrayList<>();
    }

}

