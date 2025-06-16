package backend2.persistence;

import backend2.persistence.entity.BookEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BookServiceIntegrationTest {
    @Autowired
    private BookRepository bookRepository;

    private BookEntity testBook;

    @BeforeEach
    void setUp() {
        testBook = BookEntity.builder()
                .author("Integration Author")
                .year(2023)
                .title("Integration Book")
                .genre("Science Fiction")
                .description("Integration test description.")
                .createdAt(LocalDate.now())
                .build();
    }

    @Test
    void addAndFetchBook() {
        // Add book
        BookEntity saved = bookRepository.save(testBook);
        assertNotNull(saved.getId());
        // Fetch by ID
        BookEntity byId = bookRepository.findById(saved.getId()).orElseThrow();
        assertEquals(testBook.getTitle(), byId.getTitle());
        assertEquals(testBook.getAuthor(), byId.getAuthor());
        // Fetch by genre
        List<BookEntity> byGenre = bookRepository.findByGenreAndDeletedFalse("Science Fiction");
        assertFalse(byGenre.isEmpty());
        assertTrue(byGenre.stream().anyMatch(b -> b.getId().equals(saved.getId())));
    }

    @Test
    void updateBookTest() {
        BookEntity saved = bookRepository.save(testBook);
        saved.setTitle("Updated Title");
        bookRepository.save(saved);
        BookEntity updated = bookRepository.findById(saved.getId()).orElseThrow();
        assertEquals("Updated Title", updated.getTitle());
    }

    @Test
    void deleteBookTest() {
        BookEntity saved = bookRepository.save(testBook);
        Integer id = saved.getId();
        bookRepository.deleteById(id);
        assertFalse(bookRepository.findById(id).isPresent());
    }

    @Test
    void findByGenreMultipleBooksTest() {
        BookEntity book2 = BookEntity.builder()
            .author("Another Author")
            .year(2022)
            .title("Another Book")
            .genre("Science Fiction")
            .description("Another description.")
            .createdAt(LocalDate.now())
            .build();
        bookRepository.save(testBook);
        bookRepository.save(book2);
        List<BookEntity> books = bookRepository.findByGenreAndDeletedFalse("Science Fiction");
        assertTrue(books.size() >= 2);
    }

    @Test
    void fetchNonExistentBookTest() {
        assertFalse(bookRepository.findById(-1).isPresent());
    }

    @Test
    void saveInvalidBookShouldFail() {
        BookEntity invalidBook = BookEntity.builder()
            .author(null) // Required field missing
            .year(2023)
            .title("Invalid Book")
            .genre("Science Fiction")
            .description("Missing author.")
            .createdAt(LocalDate.now())
            .build();
        assertThrows(Exception.class, () -> bookRepository.saveAndFlush(invalidBook));
    }
} 