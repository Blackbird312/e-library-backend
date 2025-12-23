package com.novelis.elibrary.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Size(min = 10, max = 13)
    @Column(unique = true)
    private String isbn;

    @NotNull
    private Integer publicationYear;

    @NotNull
    private Integer totalCopies = 1;

    @NotNull
    private Integer availableCopies = 1;

    @Nullable
    private String coverImage;

    // ===== Constructors =====
    public Book() {
    }

    public Book(String title, String isbn, Integer publicationYear, Integer totalCopies, String coverImage) {
        this.title = title;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.coverImage = coverImage;
    }

    // ===== Getters & Setters =====
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    public String getCoverImage(){return coverImage;}

    public void setCoverImage(String coverImage){this.coverImage = coverImage;}
}
