package backend2.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookDTOTest {

    @Test
    void testBookDTOBuilder() {
        // Arrange & Act
        BookDTO bookDTO = BookDTO.builder()
                .id(1)
                .author("Test Author")
                .year(2023)
                .title("Test Book")
                .genre("Horror")
                .description("Test Description")
                .build();

        // Assert
        assertNotNull(bookDTO);
        assertEquals(1, bookDTO.getId());
        assertEquals("Test Author", bookDTO.getAuthor());
        assertEquals(2023, bookDTO.getYear());
        assertEquals("Test Book", bookDTO.getTitle());
        assertEquals("Horror", bookDTO.getGenre());
        assertEquals("Test Description", bookDTO.getDescription());
    }

    @Test
    void testBookDTOAllArgsConstructor() {
        // Arrange & Act
        BookDTO bookDTO = BookDTO.builder()
                .id(1)
                .author("Test Author")
                .year(2023)
                .title("Test Book")
                .genre("Horror")
                .description("Test Description")
                .deleted(false)
                .build();

        // Assert
        assertNotNull(bookDTO);
        assertEquals(1, bookDTO.getId());
        assertEquals("Test Author", bookDTO.getAuthor());
        assertEquals(2023, bookDTO.getYear());
        assertEquals("Test Book", bookDTO.getTitle());
        assertEquals("Horror", bookDTO.getGenre());
        assertEquals("Test Description", bookDTO.getDescription());
    }

    @Test
    void testBookDTOEqualsAndHashCode() {
        // Arrange
        BookDTO bookDTO1 = BookDTO.builder()
                .id(1)
                .author("Test Author")
                .year(2023)
                .title("Test Book")
                .genre("Horror")
                .description("Test Description")
                .build();

        BookDTO bookDTO2 = BookDTO.builder()
                .id(1)
                .author("Test Author")
                .year(2023)
                .title("Test Book")
                .genre("Horror")
                .description("Test Description")
                .build();

        BookDTO bookDTO3 = BookDTO.builder()
                .id(2)
                .author("Different Author")
                .year(2024)
                .title("Different Book")
                .genre("Comedy")
                .description("Different Description")
                .build();

        // Assert
        assertEquals(bookDTO1, bookDTO2);
        assertNotEquals(bookDTO1, bookDTO3);
        assertEquals(bookDTO1.hashCode(), bookDTO2.hashCode());
        assertNotEquals(bookDTO1.hashCode(), bookDTO3.hashCode());
    }

    @Test
    void testBookDTOToString() {
        // Arrange
        BookDTO bookDTO = BookDTO.builder()
                .id(1)
                .author("Test Author")
                .year(2023)
                .title("Test Book")
                .genre("Horror")
                .description("Test Description")
                .build();

        // Act
        String toString = bookDTO.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("author=Test Author"));
        assertTrue(toString.contains("year=2023"));
        assertTrue(toString.contains("title=Test Book"));
        assertTrue(toString.contains("genre=Horror"));
        assertTrue(toString.contains("description=Test Description"));
    }
} 