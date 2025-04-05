package backend2.persistence;

import backend2.persistence.entity.BookEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookRepositoryTest {

    @Mock
    private JpaRepository<BookEntity, Integer> jpaRepository;

    private BookRepository bookRepository;
    private BookEntity testBookEntity1;
    private BookEntity testBookEntity2;
    private List<BookEntity> testBookEntities;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testBookEntity1 = BookEntity.builder()
                .id(1)
                .title("Test Book 1")
                .author("Test Author 1")
                .genre("Horror")
                .year(2023)
                .description("Test Description 1")
                .createdAt(LocalDate.now())
                .build();

        testBookEntity2 = BookEntity.builder()
                .id(2)
                .title("Test Book 2")
                .author("Test Author 2")
                .genre("Horror")
                .year(2023)
                .description("Test Description 2")
                .createdAt(LocalDate.now())
                .build();

        testBookEntities = Arrays.asList(testBookEntity1, testBookEntity2);
    }

    @Test
    void findByGenre_WithValidGenre_ShouldReturnMatchingBooks() {
        // Arrange
        String genre = "Horror";
        when(jpaRepository.findAll()).thenReturn(testBookEntities);

        // Act
        List<BookEntity> result = bookRepository.findByGenre(genre);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(book -> book.getGenre().equals(genre)));
    }

    @Test
    void findByGenre_WithNonExistentGenre_ShouldReturnEmptyList() {
        // Arrange
        String genre = "NonExistentGenre";
        when(jpaRepository.findAll()).thenReturn(testBookEntities);

        // Act
        List<BookEntity> result = bookRepository.findByGenre(genre);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void save_WithValidBook_ShouldReturnSavedBook() {
        // Arrange
        when(jpaRepository.save(any(BookEntity.class))).thenReturn(testBookEntity1);

        // Act
        BookEntity result = bookRepository.save(testBookEntity1);

        // Assert
        assertNotNull(result);
        assertEquals(testBookEntity1.getId(), result.getId());
        assertEquals(testBookEntity1.getTitle(), result.getTitle());
        assertEquals(testBookEntity1.getAuthor(), result.getAuthor());
        assertEquals(testBookEntity1.getGenre(), result.getGenre());
        assertEquals(testBookEntity1.getYear(), result.getYear());
        assertEquals(testBookEntity1.getDescription(), result.getDescription());
    }

    @Test
    void findById_WithValidId_ShouldReturnBook() {
        // Arrange
        when(jpaRepository.findById(anyInt())).thenReturn(Optional.of(testBookEntity1));

        // Act
        Optional<BookEntity> result = bookRepository.findById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testBookEntity1.getId(), result.get().getId());
        assertEquals(testBookEntity1.getTitle(), result.get().getTitle());
        assertEquals(testBookEntity1.getAuthor(), result.get().getAuthor());
        assertEquals(testBookEntity1.getGenre(), result.get().getGenre());
        assertEquals(testBookEntity1.getYear(), result.get().getYear());
        assertEquals(testBookEntity1.getDescription(), result.get().getDescription());
    }

    @Test
    void findById_WithInvalidId_ShouldReturnEmpty() {
        // Arrange
        when(jpaRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act
        Optional<BookEntity> result = bookRepository.findById(999);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void deleteById_WithValidId_ShouldDeleteBook() {
        // Arrange
        doNothing().when(jpaRepository).deleteById(anyInt());

        // Act & Assert
        assertDoesNotThrow(() -> bookRepository.deleteById(1));
        verify(jpaRepository, times(1)).deleteById(1);
    }

    @Test
    void existsById_WithValidId_ShouldReturnTrue() {
        // Arrange
        when(jpaRepository.existsById(anyInt())).thenReturn(true);

        // Act
        boolean result = bookRepository.existsById(1);

        // Assert
        assertTrue(result);
        verify(jpaRepository, times(1)).existsById(1);
    }

    @Test
    void existsById_WithInvalidId_ShouldReturnFalse() {
        // Arrange
        when(jpaRepository.existsById(anyInt())).thenReturn(false);

        // Act
        boolean result = bookRepository.existsById(999);

        // Assert
        assertFalse(result);
        verify(jpaRepository, times(1)).existsById(999);
    }
} 