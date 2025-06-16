package backend2.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookEntityTest {

    private BookEntity bookEntity;

    @BeforeEach
    void setUp() {
        bookEntity = new BookEntity();
    }

    @Test
    void testBookEntityBuilder() {
        LocalDate testDate = LocalDate.now();
        
        BookEntity builtEntity = BookEntity.builder()
                .id(1)
                .author("Test Author")
                .year(2023)
                .title("Test Title")
                .genre("Fiction")
                .description("Test Description")
                .createdAt(testDate)
                .build();

        assertNotNull(builtEntity);
        assertEquals(1, builtEntity.getId());
        assertEquals("Test Author", builtEntity.getAuthor());
        assertEquals(2023, builtEntity.getYear());
        assertEquals("Test Title", builtEntity.getTitle());
        assertEquals("Fiction", builtEntity.getGenre());
        assertEquals("Test Description", builtEntity.getDescription());
        assertEquals(testDate, builtEntity.getCreatedAt());
    }

    @Test
    void testBookEntitySettersAndGetters() {
        LocalDate testDate = LocalDate.now();

        bookEntity.setId(1);
        bookEntity.setAuthor("Test Author");
        bookEntity.setYear(2023);
        bookEntity.setTitle("Test Title");
        bookEntity.setGenre("Fiction");
        bookEntity.setDescription("Test Description");
        bookEntity.setCreatedAt(testDate);

        assertEquals(1, bookEntity.getId());
        assertEquals("Test Author", bookEntity.getAuthor());
        assertEquals(2023, bookEntity.getYear());
        assertEquals("Test Title", bookEntity.getTitle());
        assertEquals("Fiction", bookEntity.getGenre());
        assertEquals("Test Description", bookEntity.getDescription());
        assertEquals(testDate, bookEntity.getCreatedAt());
    }

    @Test
    void testBookEntityEqualsAndHashCode() {
        LocalDate testDate = LocalDate.now();
        
        BookEntity entity1 = BookEntity.builder()
                .id(1)
                .author("Test Author")
                .year(2023)
                .title("Test Title")
                .genre("Fiction")
                .description("Test Description")
                .createdAt(testDate)
                .build();

        BookEntity entity2 = BookEntity.builder()
                .id(1)
                .author("Test Author")
                .year(2023)
                .title("Test Title")
                .genre("Fiction")
                .description("Test Description")
                .createdAt(testDate)
                .build();

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void testBookEntityToString() {
        LocalDate testDate = LocalDate.now();
        
        BookEntity entity = BookEntity.builder()
                .id(1)
                .author("Test Author")
                .year(2023)
                .title("Test Title")
                .genre("Fiction")
                .description("Test Description")
                .createdAt(testDate)
                .build();

        String toString = entity.toString();
        
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("author=Test Author"));
        assertTrue(toString.contains("year=2023"));
        assertTrue(toString.contains("title=Test Title"));
        assertTrue(toString.contains("genre=Fiction"));
        assertTrue(toString.contains("description=Test Description"));
        assertTrue(toString.contains("createdAt=" + testDate));
    }

    @Test
    void testBookEntityNoArgsConstructor() {
        BookEntity entity = new BookEntity();
        assertNotNull(entity);
    }

    @Test
    void testBookEntityAllArgsConstructor() {
        LocalDate testDate = LocalDate.now();
        
        BookEntity entity = BookEntity.builder()
                .id(1)
                .author("Test Author")
                .year(2023)
                .title("Test Title")
                .genre("Horror")
                .description("Test Description")
                .createdAt(LocalDate.now())
                .deleted(false)
                .build();

        assertEquals(1, entity.getId());
        assertEquals("Test Author", entity.getAuthor());
        assertEquals(2023, entity.getYear());
        assertEquals("Test Title", entity.getTitle());
        assertEquals("Horror", entity.getGenre());
        assertEquals("Test Description", entity.getDescription());
        assertEquals(LocalDate.now(), entity.getCreatedAt());
        assertEquals(false, entity.getDeleted());
    }
} 