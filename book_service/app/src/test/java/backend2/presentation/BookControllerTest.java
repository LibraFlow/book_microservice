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
import org.springframework.security.core.context.SecurityContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

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
                .genre("Horror")
                .year(2023)
                .description("Test Description")
                .build();
    }

    @Test
    void createBookTest() {
        // Arrange
        mockAuthenticationWithRole("ROLE_ADMINISTRATOR");
        when(addBookUseCase.addBook(any(BookDTO.class))).thenReturn(testBookDTO);

        // Act
        ResponseEntity<BookDTO> response = bookController.createBook(testBookDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testBookDTO, response.getBody());
        verify(addBookUseCase, times(1)).addBook(any(BookDTO.class));
    }

    @Test
    void deleteBookTest() {
        // Arrange
        Integer bookId = 1;
        mockAuthenticationWithRole("ROLE_ADMINISTRATOR");
        doNothing().when(deleteBookUseCase).deleteBook(anyInt());

        // Act
        ResponseEntity<Void> response = bookController.deleteBook(bookId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deleteBookUseCase, times(1)).deleteBook(bookId);
    }

    @Test
    void getBookTest() {
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
    void getAllBooksByGenreTest() {
        // Arrange
        String genre = "Horror";
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
    void updateBookTest() {
        // Arrange
        Integer bookId = 1;
        mockAuthenticationWithRole("ROLE_ADMINISTRATOR");
        when(updateBookUseCase.updateBook(anyInt(), any(BookDTO.class))).thenReturn(testBookDTO);

        // Act
        ResponseEntity<BookDTO> response = bookController.updateBook(bookId, testBookDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testBookDTO, response.getBody());
        verify(updateBookUseCase, times(1)).updateBook(bookId, testBookDTO);
    }

    private void mockAuthenticationWithRole(String role) {
        Authentication authentication = mock(Authentication.class);
        Collection<? extends GrantedAuthority> authorities =
            Collections.singletonList(new SimpleGrantedAuthority(role));
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
} 