package com.novelis.elibrary.controller;

import com.novelis.elibrary.dto.loan.LoanRequest;
import com.novelis.elibrary.dto.loan.LoanResponse;
import com.novelis.elibrary.entity.Loan;
import com.novelis.elibrary.mapper.LoanMapper;
import com.novelis.elibrary.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin // optional
public class LoanController {

    private final LoanService loanService;
    private final LoanMapper loanMapper;

    public LoanController(LoanService loanService,
                          LoanMapper loanMapper) {
        this.loanService = loanService;
        this.loanMapper = loanMapper;
    }

    // POST /api/loans/borrow
    @PostMapping("/borrow")
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponse borrowBook(@Valid @RequestBody LoanRequest request) {
        Loan loan = loanService.borrowBook(
                request.getUserId(),
                request.getBookId(),
                request.getDueDate()
        );
        return loanMapper.toResponse(loan);
    }

    // POST /api/loans/{id}/return
    @PostMapping("/{id}/return")
    public LoanResponse returnBook(@PathVariable Long id) {
        Loan loan = loanService.returnBook(id);
        return loanMapper.toResponse(loan);
    }

    // GET /api/loans
    // Optional filters: userId, bookId
    @GetMapping
    public List<LoanResponse> getLoans(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long bookId
    ) {
        List<Loan> loans;

        if (userId != null) {
            loans = loanService.getLoansByUser(userId);
        } else if (bookId != null) {
            loans = loanService.getLoansByBook(bookId);
        } else {
            loans = loanService.getAllLoans();
        }

        return loans.stream()
                .map(loanMapper::toResponse)
                .toList();
    }
}
