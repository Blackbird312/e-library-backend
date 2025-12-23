package com.novelis.elibrary.service.book;

import com.novelis.elibrary.entity.Book;
import com.novelis.elibrary.exception.NotFoundException;
import com.novelis.elibrary.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    // Constructor injection (recommended)
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));
    }

    public Book create(Book book) {
        // here you can add validation / defaults / business rules
        return bookRepository.save(book);
    }

    public Book update(Long id, Book updated) {
        Book existing = findById(id);
        existing.setTitle(updated.getTitle());
        existing.setIsbn(updated.getIsbn());
        existing.setPublicationYear(updated.getPublicationYear());
        existing.setTotalCopies(updated.getTotalCopies());
        existing.setAvailableCopies(updated.getAvailableCopies());
        return bookRepository.save(existing);
    }

    public void delete(Long id) {
        Book existing = findById(id);
        bookRepository.delete(existing);
    }
}
