package com.novelis.elibrary.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanBookDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer availableCopies;
    private String coverImage;
}
