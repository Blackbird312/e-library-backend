package com.novelis.elibrary.service;

import com.novelis.elibrary.entity.Book;
import com.novelis.elibrary.entity.Loan;
import com.novelis.elibrary.entity.User;
import com.novelis.elibrary.exception.BusinessException;
import com.novelis.elibrary.exception.NotFoundException;
import com.novelis.elibrary.repository.BookRepository;
import com.novelis.elibrary.repository.LoanRepository;
import com.novelis.elibrary.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository,
                       UserRepository userRepository,
                       BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    // ===== Query methods =====

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public List<Loan> getLoansByUser(Long userId) {
        return loanRepository.findByUserId(userId);
    }

    public List<Loan> getLoansByBook(Long bookId) {
        return loanRepository.findByBookId(bookId);
    }

    // ===== Business methods =====

    /**
     * Borrow a book:
     * - Check user exists
     * - Check book exists
     * - Check there is at least 1 available copy
     * - Optionally, check user doesn't already have an active loan for this book
     * - Create Loan + decrease availableCopies
     */
    @Transactional
    public Loan borrowBook(Long userId, Long bookId, LocalDate dueDate) {
        User user = getUserOrThrow(userId);
        Book book = getBookOrThrow(bookId);

        if (book.getAvailableCopies() == null || book.getAvailableCopies() <= 0) {
            throw new BusinessException("No available copies for book with id " + bookId);
        }else{
            book.setAvailableCopies(book.getAvailableCopies() - 1);
        }

        // Optional rule: prevent multiple active loans for the same book by the same user
        List<Loan> existingLoansForUser = loanRepository.findByUserId(userId);
        boolean alreadyBorrowedAndNotReturned = existingLoansForUser.stream()
                .anyMatch(loan -> loan.getBook() != null
                        && loan.getBook().getId().equals(bookId)
                        && !loan.isReturned());

        if (alreadyBorrowedAndNotReturned) {
            throw new BusinessException("User " + userId + " already has an active loan for book " + bookId);
        }

        if (dueDate == null) {
            throw new BusinessException("Due date is required");
        }
        if (dueDate.isBefore(LocalDate.now())) {
            throw new BusinessException("Due date cannot be in the past");
        }

        // Create Loan
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(dueDate);
        loan.setReturnDate(null);

        // Update book copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);

        // Save both (same transaction)
        bookRepository.save(book);
        return loanRepository.save(loan);
    }

    /**
     * Return a book:
     * - Check loan exists
     * - Check it's not already returned
     * - Set returnDate
     * - Increase book.availableCopies
     */
    @Transactional
    public Loan returnBook(Long loanId) {
        Loan loan = getLoanOrThrow(loanId);

        if (loan.isReturned()) {
            throw new BusinessException("Loan " + loanId + " is already returned");
        }

        Book book = loan.getBook();
        if (book == null) {
            throw new BusinessException("Loan " + loanId + " has no associated book");
        }

        // Mark as returned
        loan.setReturnDate(LocalDate.now());

        // Increase available copies
        Integer currentAvailable = book.getAvailableCopies();
        if (currentAvailable == null) {
            currentAvailable = 0;
        }
        book.setAvailableCopies(currentAvailable + 1);

        bookRepository.save(book);
        return loanRepository.save(loan);
    }

    // ===== Helper methods =====

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id " + userId));
    }

    private Book getBookOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found with id " + bookId));
    }

    private Loan getLoanOrThrow(Long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found with id " + loanId));
    }
}
