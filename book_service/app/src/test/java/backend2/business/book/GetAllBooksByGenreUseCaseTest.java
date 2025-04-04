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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllBooksByGenreUseCaseTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private GetAllBooksByGenreUseCase getAllBooksByGenreUseCase;

    private BookEntity testBookEntity1;
    private BookEntity testBookEntity2;
    private BookDTO testBookDTO1;
    private BookDTO testBookDTO2;
    private List<BookEntity> testBookEntities;
    private List<BookDTO> expectedBookDTOs;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testBookEntity1 = new BookEntity();
        testBookEntity1.setId(1);
        testBookEntity1.setTitle("Test Book 1");
        testBookEntity1.setAuthor("Test Author 1");
        testBookEntity1.setGenre("Horror");
        testBookEntity1.setYear(2023);
        testBookEntity1.setDescription("Test Description 1");

        testBookEntity2 = new BookEntity();
        testBookEntity2.setId(2);
        testBookEntity2.setTitle("Test Book 2");
        testBookEntity2.setAuthor("Test Author 2");
        testBookEntity2.setGenre("Horror");
        testBookEntity2.setYear(2023);
        testBookEntity2.setDescription("Test Description 2");

        testBookDTO1 = BookDTO.builder()
                .id(1)
                .title("Test Book 1")
                .author("Test Author 1")
                .genre("Horror")
                .year(2023)
                .description("Test Description 1")
                .build();

        testBookDTO2 = BookDTO.builder()
                .id(2)
                .title("Test Book 2")
                .author("Test Author 2")
                .genre("Horror")
                .year(2023)
                .description("Test Description 2")
                .build();

        testBookEntities = Arrays.asList(testBookEntity1, testBookEntity2);
        expectedBookDTOs = Arrays.asList(testBookDTO1, testBookDTO2);
    }

    @Test
    void getAllBooksByGenre_Success() {
        // Arrange
        String genre = "Horror";
        when(bookRepository.findByGenre(genre)).thenReturn(testBookEntities);
        when(bookMapper.toDTO(testBookEntity1)).thenReturn(testBookDTO1);
        when(bookMapper.toDTO(testBookEntity2)).thenReturn(testBookDTO2);

        // Act
        List<BookDTO> result = getAllBooksByGenreUseCase.getAllBooksByGenre(genre);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedBookDTOs, result);

        // Verify interactions
        verify(bookRepository, times(1)).findByGenre(genre);
        verify(bookMapper, times(1)).toDTO(testBookEntity1);
        verify(bookMapper, times(1)).toDTO(testBookEntity2);
    }

    @Test
    void getAllBooksByGenre_EmptyResult() {
        // Arrange
        String genre = "NonExistentGenre";
        when(bookRepository.findByGenre(genre)).thenReturn(Arrays.asList());

        // Act
        List<BookDTO> result = getAllBooksByGenreUseCase.getAllBooksByGenre(genre);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify interactions
        verify(bookRepository, times(1)).findByGenre(genre);
        verify(bookMapper, never()).toDTO(any());
    }

    @Test
    void getAllBooksByGenre_WithNullGenre_ShouldReturnEmptyList() {
        // Act
        List<BookDTO> result = getAllBooksByGenreUseCase.getAllBooksByGenre(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify interactions
        verify(bookRepository, times(1)).findByGenre(null);
        verify(bookMapper, never()).toDTO(any());
    }
} 