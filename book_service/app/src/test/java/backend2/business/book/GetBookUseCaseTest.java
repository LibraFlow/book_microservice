package backend2.business.book;

import backend2.domain.BookDTO;
import backend2.persistence.BookRepository;
import backend2.persistence.entity.BookEntity;
import backend2.business.mapper.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBookUseCaseTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private GetBookUseCase getBookUseCase;

    private BookDTO testBookDTO;
    private BookEntity testBookEntity;
    private Integer testBookId;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testBookId = 1;
        
        testBookDTO = BookDTO.builder()
                .id(testBookId)
                .title("Test Book")
                .author("Test Author")
                .genre("Horror")
                .year(2023)
                .description("Test Description")
                .build();

        testBookEntity = new BookEntity();
        testBookEntity.setId(testBookId);
        testBookEntity.setTitle("Test Book");
        testBookEntity.setAuthor("Test Author");
        testBookEntity.setGenre("Horror");
        testBookEntity.setYear(2023);
        testBookEntity.setDescription("Test Description");
    }

    @Test
    void getBook_Success() {
        // Arrange
        when(bookRepository.findById(testBookId)).thenReturn(Optional.of(testBookEntity));
        when(bookMapper.toDTO(testBookEntity)).thenReturn(testBookDTO);

        // Act
        BookDTO result = getBookUseCase.getBook(testBookId);

        // Assert
        assertNotNull(result);
        assertEquals(testBookDTO.getId(), result.getId());
        assertEquals(testBookDTO.getTitle(), result.getTitle());
        assertEquals(testBookDTO.getAuthor(), result.getAuthor());
        assertEquals(testBookDTO.getGenre(), result.getGenre());
        assertEquals(testBookDTO.getYear(), result.getYear());
        assertEquals(testBookDTO.getDescription(), result.getDescription());

        // Verify interactions
        verify(bookRepository, times(1)).findById(testBookId);
        verify(bookMapper, times(1)).toDTO(testBookEntity);
    }

    @Test
    void getBook_NotFound_ShouldThrowException() {
        // Arrange
        when(bookRepository.findById(testBookId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            getBookUseCase.getBook(testBookId);
        });

        assertEquals("Book not found with id: " + testBookId, exception.getMessage());

        // Verify interactions
        verify(bookRepository, times(1)).findById(testBookId);
        verifyNoInteractions(bookMapper);
    }
} 