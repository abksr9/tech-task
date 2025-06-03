package com.acme.bookmanagement.controller;

import com.acme.bookmanagement.model.Author;
import com.acme.bookmanagement.model.Book;
import com.acme.bookmanagement.model.BookInput;
import com.acme.bookmanagement.service.BookService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/graphql")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @QueryMapping
    public List<Book> findAllBooks() {
        return bookService.findAll();
    }

    @MutationMapping
    public Book createBook(@Argument("input") BookInput bookInput) {
        Book book = new Book();
        book.setTitle(bookInput.getTitle());
        book.setPublishedDate(LocalDate.parse(bookInput.getPublishedDate()));
        book.setAuthor(new Author(bookInput.getAuthorName()));
        try {
            return bookService.saveBook(book);
        } catch (Exception e) {
            System.out.println("Error saving book: " + e.getMessage());
            throw new RuntimeException("Error saving book: " + e.getMessage(), e);
        }

    }
    @QueryMapping
    public List<Book> findBooksByDateRange(@Argument String startDate, @Argument String endDate) {
        return bookService.findBooksByDateRange(startDate, endDate);
    }

}
