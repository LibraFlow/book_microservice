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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddBookUseCaseTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private AddBookUseCase addBookUseCase;

    private BookDTO testBookDTO;
    private BookEntity testBookEntity;
    private BookEntity savedBookEntity;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testBookDTO = BookDTO.builder()
                .id(1)
                .title("Test Book")
                .author("Test Author")
                .genre("Horror")
                .year(2023)
                .description("Test Description")
                .build();

        testBookEntity = new BookEntity();
        testBookEntity.setId(1);
        testBookEntity.setTitle("Test Book");
        testBookEntity.setAuthor("Test Author");
        testBookEntity.setGenre("Horror");
        testBookEntity.setYear(2023);
        testBookEntity.setDescription("Test Description");

        savedBookEntity = new BookEntity();
        savedBookEntity.setId(1);
        savedBookEntity.setTitle("Test Book");
        savedBookEntity.setAuthor("Test Author");
        savedBookEntity.setGenre("Horror");
        savedBookEntity.setYear(2023);
        savedBookEntity.setDescription("Test Description");
    }

    @Test
    void addBook_Success() {
        // Arrange
        when(bookMapper.toEntity(any(BookDTO.class))).thenReturn(testBookEntity);
        when(bookRepository.save(any(BookEntity.class))).thenReturn(savedBookEntity);
        when(bookMapper.toDTO(any(BookEntity.class))).thenReturn(testBookDTO);

        // Act
        BookDTO result = addBookUseCase.addBook(testBookDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testBookDTO.getId(), result.getId());
        assertEquals(testBookDTO.getTitle(), result.getTitle());
        assertEquals(testBookDTO.getAuthor(), result.getAuthor());
        assertEquals(testBookDTO.getGenre(), result.getGenre());
        assertEquals(testBookDTO.getYear(), result.getYear());
        assertEquals(testBookDTO.getDescription(), result.getDescription());

        // Verify interactions
        verify(bookMapper, times(1)).toEntity(testBookDTO);
        verify(bookRepository, times(1)).save(testBookEntity);
        verify(bookMapper, times(1)).toDTO(savedBookEntity);
    }

    @Test
    void addBook_WithNullInput_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            addBookUseCase.addBook(null);
        });

        // Verify no interactions with dependencies
        verifyNoInteractions(bookRepository);
        verifyNoInteractions(bookMapper);
    }
} 