package com.novelis.elibrary.service.book;

import com.novelis.elibrary.entity.Book;
import com.novelis.elibrary.exception.NotFoundException;
import com.novelis.elibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class BookImageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private final BookRepository bookRepository;

    // Allow only common safe image types
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    // Max 5 MB (adjust as you want)
    private static final long MAX_SIZE_BYTES = 10L * 1024 * 1024;

    public BookImageService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Upload/replace the book cover image.
     * Stores the file on disk, saves the public URL in Book.coverImage.
     */
    public Book updateCover(Long id, MultipartFile file) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));

        String filename = saveFile(file, id);

        // Optional: delete old cover file when replacing
        deleteOldCoverIfExists(book.getCoverImage());

        book.setCoverImage("/uploads/" + filename);
        return bookRepository.save(book);
    }

    private String saveFile(MultipartFile file, Long bookId) {
        validateFile(file);

        String originalName = StringUtils.cleanPath(
                Objects.requireNonNullElse(file.getOriginalFilename(), "cover")
        );

        String ext = extractExtension(originalName, file.getContentType());
        String filename = "book-" + bookId + "-" + UUID.randomUUID() + ext;

        Path uploadPath = Path.of(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath);

            Path target = uploadPath.resolve(filename).normalize();

            // Safety: ensure no path traversal
            if (!target.startsWith(uploadPath)) {
                throw new IllegalArgumentException("Invalid file path");
            }

            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }

            return filename;
        } catch (Exception e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new IllegalArgumentException("File too large. Max is " + (MAX_SIZE_BYTES / (1024 * 1024)) + "MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Invalid file type. Allowed: JPG, PNG, WEBP");
        }
    }

    private String extractExtension(String originalName, String contentType) {
        // Prefer filename extension if present
        int dot = originalName.lastIndexOf('.');
        if (dot >= 0 && dot < originalName.length() - 1) {
            String ext = originalName.substring(dot).toLowerCase();
            if (ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".png") || ext.equals(".webp")) {
                return ext.equals(".jpeg") ? ".jpg" : ext;
            }
        }

        // Fallback to content-type
        return switch (contentType) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> ".jpg"; // image/jpeg
        };
    }

    private void deleteOldCoverIfExists(String coverImageUrl) {
        if (coverImageUrl == null || coverImageUrl.isBlank()) {
            return;
        }

        // We store URLs like "/uploads/filename.ext"
        String prefix = "/uploads/";
        if (!coverImageUrl.startsWith(prefix)) {
            return;
        }

        String filename = coverImageUrl.substring(prefix.length()).trim();
        if (filename.isBlank()) {
            return;
        }

        Path uploadPath = Path.of(uploadDir).toAbsolutePath().normalize();
        Path oldFile = uploadPath.resolve(filename).normalize();

        // Safety: ensure no path traversal
        if (!oldFile.startsWith(uploadPath)) {
            return;
        }

        try {
            Files.deleteIfExists(oldFile);
        } catch (Exception ignored) {
            // Intentionally ignore delete failures to avoid breaking upload flow
        }
    }
}
