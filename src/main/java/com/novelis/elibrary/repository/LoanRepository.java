package com.novelis.elibrary.repository;

import com.novelis.elibrary.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByUserId(Long userId);

    List<Loan> findByBookId(Long bookId);

    int countByUserId(Long userId);

    List<Loan> findByUser_EmailOrderByLoanDateDesc(String email);
}
