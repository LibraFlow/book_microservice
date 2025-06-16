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
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBookUseCaseTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private UpdateBookUseCase updateBookUseCase;

    private BookDTO testBookDTO;
    private BookEntity existingBookEntity;
    private BookEntity updatedBookEntity;
    private BookEntity savedBookEntity;
    private LocalDate testCreatedAt;
    private BookEntity testBookEntity;

    @BeforeEach
    void setUp() {
        testCreatedAt = LocalDate.now();
        
        // Initialize test data
        testBookDTO = BookDTO.builder()
                .id(1)
                .title("Updated Book")
                .author("Updated Author")
                .genre("Science Fiction")
                .year(2024)
                .description("Updated Description")
                .build();

        existingBookEntity = new BookEntity();
        existingBookEntity.setId(1);
        existingBookEntity.setTitle("Original Book");
        existingBookEntity.setAuthor("Original Author");
        existingBookEntity.setGenre("Horror");
        existingBookEntity.setYear(2023);
        existingBookEntity.setDescription("Original Description");
        existingBookEntity.setCreatedAt(testCreatedAt);

        updatedBookEntity = new BookEntity();
        updatedBookEntity.setId(1);
        updatedBookEntity.setTitle("Updated Book");
        updatedBookEntity.setAuthor("Updated Author");
        updatedBookEntity.setGenre("Science Fiction");
        updatedBookEntity.setYear(2024);
        updatedBookEntity.setDescription("Updated Description");
        updatedBookEntity.setCreatedAt(testCreatedAt);

        savedBookEntity = new BookEntity();
        savedBookEntity.setId(1);
        savedBookEntity.setTitle("Updated Book");
        savedBookEntity.setAuthor("Updated Author");
        savedBookEntity.setGenre("Science Fiction");
        savedBookEntity.setYear(2024);
        savedBookEntity.setDescription("Updated Description");
        savedBookEntity.setCreatedAt(testCreatedAt);

        testBookEntity = BookEntity.builder().id(1).title("Test Book").build();
    }

    @Test
    void updateBook_Success() {
        // Arrange
        when(bookRepository.findById(1)).thenReturn(Optional.of(existingBookEntity));
        when(bookMapper.toEntity(any(BookDTO.class))).thenReturn(updatedBookEntity);
        when(bookRepository.save(any(BookEntity.class))).thenReturn(savedBookEntity);
        when(bookMapper.toDTO(any(BookEntity.class))).thenReturn(testBookDTO);

        // Act
        BookDTO result = updateBookUseCase.updateBook(1, testBookDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testBookDTO.getId(), result.getId());
        assertEquals(testBookDTO.getTitle(), result.getTitle());
        assertEquals(testBookDTO.getAuthor(), result.getAuthor());
        assertEquals(testBookDTO.getGenre(), result.getGenre());
        assertEquals(testBookDTO.getYear(), result.getYear());
        assertEquals(testBookDTO.getDescription(), result.getDescription());

        // Verify interactions
        verify(bookRepository, times(1)).findById(1);
        verify(bookMapper, times(1)).toEntity(testBookDTO);
        verify(bookRepository, times(1)).save(any(BookEntity.class));
        verify(bookMapper, times(1)).toDTO(savedBookEntity);
    }

    @Test
    void updateBook_BookNotFound_ShouldThrowException() {
        // Arrange
        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            updateBookUseCase.updateBook(1, testBookDTO);
        });

        assertEquals("Book not found with id: 1", exception.getMessage());

        // Verify interactions
        verify(bookRepository, times(1)).findById(1);
        verifyNoInteractions(bookMapper);
        verify(bookRepository, never()).save(any(BookEntity.class));
    }

    @Test
    void updateBook_PreservesCreatedAt() {
        // Arrange
        when(bookRepository.findById(1)).thenReturn(Optional.of(existingBookEntity));
        when(bookMapper.toEntity(any(BookDTO.class))).thenReturn(updatedBookEntity);
        when(bookRepository.save(any(BookEntity.class))).thenReturn(savedBookEntity);
        when(bookMapper.toDTO(any(BookEntity.class))).thenReturn(testBookDTO);

        // Act
        updateBookUseCase.updateBook(1, testBookDTO);

        // Verify that the created date was preserved
        verify(bookRepository).save(argThat(entity -> 
            entity.getCreatedAt().equals(testCreatedAt)
        ));
    }

    @Test
    void updateBook_ShouldLogAuditTrail() {
        // Arrange
        Logger logger = (Logger) LoggerFactory.getLogger(UpdateBookUseCase.class);
        @SuppressWarnings("unchecked")
        Appender<ILoggingEvent> mockAppender = mock(Appender.class);
        logger.addAppender(mockAppender);

        when(bookRepository.findById(anyInt())).thenReturn(java.util.Optional.of(testBookEntity));
        when(bookMapper.toEntity(any(BookDTO.class))).thenReturn(testBookEntity);
        when(bookRepository.save(any(BookEntity.class))).thenReturn(testBookEntity);
        when(bookMapper.toDTO(any(BookEntity.class))).thenReturn(testBookDTO);

        // Act
        updateBookUseCase.updateBook(1, testBookDTO);

        // Assert: verify that the audit log message was produced
        verify(mockAppender, times(1)).doAppend(argThat(event ->
            event.getFormattedMessage().contains("AUDIT: Book updated") &&
            event.getFormattedMessage().contains("bookId=" + testBookEntity.getId())
        ));
        logger.detachAppender(mockAppender);
    }
} 