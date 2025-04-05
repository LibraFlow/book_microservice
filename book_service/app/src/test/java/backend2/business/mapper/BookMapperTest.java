package backend2.business.mapper;

import backend2.domain.BookDTO;
import backend2.persistence.entity.BookEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookMapperTest {

    @InjectMocks
    private BookMapper bookMapper;

    private BookEntity testBookEntity;
    private BookDTO testBookDTO;

    @BeforeEach
    void setUp() {
        // Initialize test data for BookEntity
        testBookEntity = BookEntity.builder()
                .id(1)
                .title("Test Book")
                .author("Test Author")
                .genre("Horror")
                .year(2023)
                .description("Test Description")
                .createdAt(LocalDate.now())
                .build();

        // Initialize test data for BookDTO
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
    void toDTO_WithValidEntity_ShouldReturnCorrectDTO() {
        // Act
        BookDTO result = bookMapper.toDTO(testBookEntity);

        // Assert
        assertNotNull(result);
        assertEquals(testBookEntity.getId(), result.getId());
        assertEquals(testBookEntity.getTitle(), result.getTitle());
        assertEquals(testBookEntity.getAuthor(), result.getAuthor());
        assertEquals(testBookEntity.getGenre(), result.getGenre());
        assertEquals(testBookEntity.getYear(), result.getYear());
        assertEquals(testBookEntity.getDescription(), result.getDescription());
    }

    @Test
    void toDTO_WithNullEntity_ShouldReturnNull() {
        // Act
        BookDTO result = bookMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDTO_WithHtmlContent_ShouldEncodeHtml() {
        // Arrange
        BookEntity entityWithHtml = BookEntity.builder()
                .id(1)
                .title("<script>alert('xss')</script>")
                .author("<b>Author</b>")
                .genre("<i>Genre</i>")
                .description("<p>Description</p>")
                .year(2023)
                .build();

        // Act
        BookDTO result = bookMapper.toDTO(entityWithHtml);

        // Assert
        assertNotNull(result);
        assertFalse(result.getTitle().contains("<script>"));
        assertFalse(result.getAuthor().contains("<b>"));
        assertFalse(result.getGenre().contains("<i>"));
        assertFalse(result.getDescription().contains("<p>"));
    }

    @Test
    void toEntity_WithValidDTO_ShouldReturnCorrectEntity() {
        // Act
        BookEntity result = bookMapper.toEntity(testBookDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testBookDTO.getId(), result.getId());
        assertEquals(testBookDTO.getTitle(), result.getTitle());
        assertEquals(testBookDTO.getAuthor(), result.getAuthor());
        assertEquals(testBookDTO.getGenre(), result.getGenre());
        assertEquals(testBookDTO.getYear(), result.getYear());
        assertEquals(testBookDTO.getDescription(), result.getDescription());
    }

    @Test
    void toEntity_WithNullDTO_ShouldReturnNull() {
        // Act
        BookEntity result = bookMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WithNewBook_ShouldSetCreatedAt() {
        // Arrange
        BookDTO newBookDTO = BookDTO.builder()
                .title("New Book")
                .author("New Author")
                .genre("Fiction")
                .year(2024)
                .description("New Description")
                .build();

        // Act
        BookEntity result = bookMapper.toEntity(newBookDTO);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getCreatedAt());
        assertEquals(LocalDate.now(), result.getCreatedAt());
    }

    @Test
    void toEntity_WithExistingBook_ShouldNotSetCreatedAt() {
        // Act
        BookEntity result = bookMapper.toEntity(testBookDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getCreatedAt());
    }
} 