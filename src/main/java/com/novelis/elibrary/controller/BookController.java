package com.novelis.elibrary.controller;

import com.novelis.elibrary.dto.book.BookRequest;
import com.novelis.elibrary.dto.book.BookResponse;
import com.novelis.elibrary.entity.Book;
import com.novelis.elibrary.mapper.BookMapper;
import com.novelis.elibrary.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService,
                          BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @GetMapping
    public List<BookResponse> getAllBooks() {
        return bookService.findAll()
                .stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public BookResponse getBookById(@PathVariable Long id) {
        Book book = bookService.findById(id);
        return bookMapper.toResponse(book);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(@Valid @RequestBody BookRequest request) {
        Book book = bookMapper.toEntity(request);
        Book saved = bookService.create(book);
        return bookMapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    public BookResponse updateBook(@PathVariable Long id,
                                   @Valid @RequestBody BookRequest request) {
        Book existing = bookService.findById(id);
        bookMapper.updateEntityFromRequest(request, existing);
        Book updated = bookService.update(id, existing);
        return bookMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}
