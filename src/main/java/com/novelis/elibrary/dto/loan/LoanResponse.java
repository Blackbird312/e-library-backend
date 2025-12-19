package com.novelis.elibrary.dto.loan;


import java.time.LocalDate;

public class LoanResponse {

    private Long id;
    private LoanUserDTO user;
    private LoanBookDTO book;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean returned;



    // ==== Getters & Setters ====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public LoanUserDTO getUser() {
        return user;
    }

    public void setUser(LoanUserDTO user) {
        this.user = user;
    }

    public LoanBookDTO getBook() {
        return book;
    }

    public void setBook(LoanBookDTO book) {
        this.book = book;
    }
}
