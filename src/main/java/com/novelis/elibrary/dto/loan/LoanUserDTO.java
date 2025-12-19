package com.novelis.elibrary.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanUserDTO {
    private Long id;
    private String fullName;
    private String email;
    private LocalDate membershipDate;
}
