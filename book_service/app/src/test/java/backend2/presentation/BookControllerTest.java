package backend2.presentation;

import backend2.domain.BookDTO;
import backend2.business.book.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Mock
    private AddBookUseCase addBookUseCase;

    @Mock
    private DeleteBookUseCase deleteBookUseCase;

    @Mock
    private GetBookUseCase getBookUseCase;

    @Mock
    private GetAllBooksByGenreUseCase getAllBooksByGenreUseCase;

    @Mock
    private UpdateBookUseCase updateBookUseCase;

    @InjectMocks
    private BookController bookController;

    private BookDTO testBookDTO;

    @BeforeEach
    void setUp() {
        testBookDTO = BookDTO.builder()
                .id(1)
                .title("Test Book")
                .author("Test Author")
                .year(2023)
                .genre("Fiction")
                .description("Test Description")
                .build();
    }

    @Test
    void createBook_ShouldReturnCreatedBook() {
        // Arrange
        when(addBookUseCase.addBook(any(BookDTO.class))).thenReturn(testBookDTO);

        // Act
        ResponseEntity<BookDTO> response = bookController.createBook(testBookDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testBookDTO, response.getBody());
        verify(addBookUseCase, times(1)).addBook(testBookDTO);
    }

    @Test
    void deleteBook_ShouldReturnNoContent() {
        // Arrange
        Integer bookId = 1;
        doNothing().when(deleteBookUseCase).deleteBook(anyInt());

        // Act
        ResponseEntity<Void> response = bookController.deleteBook(bookId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deleteBookUseCase, times(1)).deleteBook(bookId);
    }

    @Test
    void getBook_ShouldReturnBook() {
        // Arrange
        Integer bookId = 1;
        when(getBookUseCase.getBook(anyInt())).thenReturn(testBookDTO);

        // Act
        ResponseEntity<BookDTO> response = bookController.getBook(bookId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testBookDTO, response.getBody());
        verify(getBookUseCase, times(1)).getBook(bookId);
    }

    @Test
    void getAllBooksByGenre_ShouldReturnBooks() {
        // Arrange
        String genre = "Fiction";
        List<BookDTO> expectedBooks = Arrays.asList(testBookDTO);
        when(getAllBooksByGenreUseCase.getAllBooksByGenre(anyString())).thenReturn(expectedBooks);

        // Act
        ResponseEntity<List<BookDTO>> response = bookController.getAllBooksByGenre(genre);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBooks, response.getBody());
        assertEquals(1, response.getBody().size());
        verify(getAllBooksByGenreUseCase, times(1)).getAllBooksByGenre(genre);
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook() {
        // Arrange
        Integer bookId = 1;
        when(updateBookUseCase.updateBook(anyInt(), any(BookDTO.class))).thenReturn(testBookDTO);

        // Act
        ResponseEntity<BookDTO> response = bookController.updateBook(bookId, testBookDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testBookDTO, response.getBody());
        verify(updateBookUseCase, times(1)).updateBook(bookId, testBookDTO);
    }
}