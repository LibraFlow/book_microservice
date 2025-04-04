package backend2.business.book;

import backend2.persistence.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteBookUseCaseTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private DeleteBookUseCase deleteBookUseCase;

    private Integer testBookId;

    @BeforeEach
    void setUp() {
        testBookId = 1;
    }

    @Test
    void deleteBook_Success() {
        // Arrange
        when(bookRepository.existsById(testBookId)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(testBookId);

        // Act
        Void result = deleteBookUseCase.deleteBook(testBookId);

        // Assert
        assertNull(result);
        verify(bookRepository, times(1)).existsById(testBookId);
        verify(bookRepository, times(1)).deleteById(testBookId);
    }

    @Test
    void deleteBook_BookNotFound_ShouldThrowException() {
        // Arrange
        when(bookRepository.existsById(testBookId)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            deleteBookUseCase.deleteBook(testBookId);
        });

        assertEquals("Book not found with id: " + testBookId, exception.getMessage());
        verify(bookRepository, times(1)).existsById(testBookId);
        verify(bookRepository, never()).deleteById(any());
    }
} 