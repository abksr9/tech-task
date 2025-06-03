package com.acme.bookmanagement.service;

import com.acme.bookmanagement.model.Author;
import com.acme.bookmanagement.model.Book;
import com.acme.bookmanagement.repository.AuthorRepository;
import com.acme.bookmanagement.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookServiceTest {
    private final BookRepository bookRepository = Mockito.mock(BookRepository.class);
    private final AuthorRepository authorRepository = Mockito.mock(AuthorRepository.class);
    private final BookService bookService = new BookService(bookRepository, authorRepository);

    private final Book book = new Book(1L,
            "title-1",
            new Author("author-1"),
            LocalDate.of(2021, 2, 3));

    @Test
    void testFindAll() {
        Mockito.when(bookRepository.findAll()).thenReturn(Collections.singletonList(book));
        assertEquals(1, bookService.findAll().size());
    }
    @Test
    void testSaveBook_NewAuthor() {
        // Arrange
        Author author = new Author();
        author.setName("John Doe");
        Book book = new Book();
        book.setAuthor(author);

        when(authorRepository.findByName("John Doe")).thenReturn(Optional.empty());
        when(authorRepository.save(author)).thenReturn(author);
        when(bookRepository.save(book)).thenReturn(book);

        // Act
        Book savedBook = bookService.saveBook(book);

        // Assert
        assertEquals(author, savedBook.getAuthor());
        verify(authorRepository, times(1)).findByName("John Doe");
        verify(authorRepository, times(1)).save(author);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testSaveBook_ExistingAuthor() {
        // Arrange
        Author author = new Author();
        author.setName("John Doe");
        Author existingAuthor = new Author();
        existingAuthor.setName("John Doe");
        Book book = new Book();
        book.setAuthor(author);

        when(authorRepository.findByName("John Doe")).thenReturn(Optional.of(existingAuthor));
        when(bookRepository.save(book)).thenReturn(book);

        // Act
        Book savedBook = bookService.saveBook(book);

        // Assert
        assertEquals(existingAuthor, savedBook.getAuthor());
        verify(authorRepository, times(1)).findByName("John Doe");
        verify(authorRepository, never()).save(author);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testFindBooksByPublishedDateBetween() {
        // Arrange
        String start = "2023-01-01";
        String end = "2023-12-31";
        Book book1 = new Book();
        Book book2 = new Book();

        when(bookRepository.findByPublishedDateBetween(LocalDate.parse(start), LocalDate.parse(end))).thenReturn(Arrays.asList(book1, book2));

        // Act
        List<Book> books = bookService.findBooksByDateRange(start, end);

        // Assert
        assertEquals(2, books.size());
        verify(bookRepository, times(1)).findByPublishedDateBetween(LocalDate.parse(start), LocalDate.parse(end));
    }
}
