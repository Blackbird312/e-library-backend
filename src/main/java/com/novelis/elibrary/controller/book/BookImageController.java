package com.novelis.elibrary.controller.book;

import com.novelis.elibrary.entity.Book;
import com.novelis.elibrary.repository.BookRepository;
import com.novelis.elibrary.service.book.BookImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
public class BookImageController {

    private final BookImageService bookImageService;

    public BookImageController(BookImageService bookImageService) {
        this.bookImageService = bookImageService;
    }

    @PostMapping(value = "/{id}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Book> uploadCover(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        Book updated = bookImageService.updateCover(id, file);
        return ResponseEntity.ok(updated);
    }
}

