package backend2.business.book;

import backend2.persistence.BookRepository;
import backend2.persistence.entity.BookEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeleteBookUseCaseTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private DeleteBookUseCase deleteBookUseCase;

    @BeforeEach
    void setUp() {
        deleteBookUseCase = new DeleteBookUseCase(bookRepository, kafkaTemplate, objectMapper);
    }

    @Test
    void deleteBook_Success() throws Exception {
        // Arrange
        Integer bookId = 1;
        BookEntity book = BookEntity.builder()
                .id(bookId)
                .author("Test Author")
                .title("Test Title")
                .genre("Test Genre")
                .year(2023)
                .description("Test Description")
                .deleted(false)
                .build();
        when(bookRepository.findByIdAndDeletedFalse(bookId)).thenReturn(book);
        when(bookRepository.save(any(BookEntity.class))).thenReturn(book);
        when(objectMapper.writeValueAsString(bookId)).thenReturn("1");

        // Act
        deleteBookUseCase.deleteBook(bookId);

        // Assert
        verify(bookRepository).findByIdAndDeletedFalse(bookId);
        verify(bookRepository).save(argThat(savedBook -> 
            savedBook.getId().equals(bookId) && savedBook.getDeleted()
        ));
        verify(objectMapper).writeValueAsString(bookId);
        verify(kafkaTemplate).send(eq("book.deleted"), eq("1"));
    }

    @Test
    void deleteBook_BookNotFound_ShouldThrowException() {
        // Arrange
        Integer bookId = 1;
        when(bookRepository.findByIdAndDeletedFalse(bookId)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            deleteBookUseCase.deleteBook(bookId);
        });

        verify(bookRepository).findByIdAndDeletedFalse(bookId);
        verify(bookRepository, never()).save(any(BookEntity.class));
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }
} 