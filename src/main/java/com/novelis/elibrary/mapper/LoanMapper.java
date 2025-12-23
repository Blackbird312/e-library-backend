package com.novelis.elibrary.mapper;

import com.novelis.elibrary.dto.loan.LoanBookDTO;
import com.novelis.elibrary.dto.loan.LoanResponse;
import com.novelis.elibrary.dto.loan.LoanUserDTO;
import com.novelis.elibrary.entity.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    public LoanResponse toResponse(Loan loan) {
        LoanResponse dto = new LoanResponse();
        dto.setId(loan.getId());

        if (loan.getUser() != null) {
            LoanUserDTO userDTO = new LoanUserDTO();
            userDTO.setId(loan.getUser().getId());
            userDTO.setFullName(loan.getUser().getFullName());
            userDTO.setEmail(loan.getUser().getEmail());
            userDTO.setMembershipDate(loan.getUser().getMembershipDate());
            dto.setUser(userDTO);
        }

        if (loan.getBook() != null) {
//            dto.setBookId(loan.getBook().getId());
//            dto.setBookTitle(loan.getBook().getTitle());
            LoanBookDTO bookDTO = new LoanBookDTO();
            bookDTO.setId(loan.getBook().getId());
            bookDTO.setTitle(loan.getBook().getTitle());
//            bookDTO.setAuthor(loan.getBook().getAuthor());
            bookDTO.setIsbn(loan.getBook().getIsbn());
            bookDTO.setAvailableCopies(loan.getBook().getAvailableCopies());
            bookDTO.setCoverImage(loan.getBook().getCoverImage());
            dto.setBook(bookDTO);
        }

        dto.setLoanDate(loan.getLoanDate());
        dto.setDueDate(loan.getDueDate());
        dto.setReturnDate(loan.getReturnDate());
        dto.setReturned(loan.isReturned());

        return dto;
    }
}
