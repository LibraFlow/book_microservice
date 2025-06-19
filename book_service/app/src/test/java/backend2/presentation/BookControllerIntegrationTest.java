package backend2.presentation;

import backend2.domain.BookDTO;
import backend2.persistence.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testDatabaseConnection() {
        // Simple test to verify database connectivity
        assertNotNull(bookRepository, "BookRepository should be autowired");
        // Try to count books (this will test the database connection)
        long count = bookRepository.count();
        System.out.println("Database connection test: Found " + count + " books in database");
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    public void testCreateBook() throws Exception {
        // Create a test book
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Test Book");
        bookDTO.setAuthor("Test Author");
        bookDTO.setYear(2024);
        bookDTO.setGenre("Fiction");
        bookDTO.setDescription("A test book description");

        // Convert book to JSON
        String bookJson = objectMapper.writeValueAsString(bookDTO);

        // Perform the request
        MvcResult result = mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // Verify the response
        String responseBody = result.getResponse().getContentAsString();
        assertNotNull(responseBody, "Response body should not be null");
        assertTrue(responseBody.contains("Test Book"), "Response should contain the book title");
        assertTrue(responseBody.contains("Test Author"), "Response should contain the author");
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void testCreateBookUnauthorized() throws Exception {
        // Create a test book
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Test Book");
        bookDTO.setAuthor("Test Author");
        bookDTO.setYear(2024);
        bookDTO.setGenre("Fiction");
        bookDTO.setDescription("A test book description");

        // Convert book to JSON
        String bookJson = objectMapper.writeValueAsString(bookDTO);

        // Perform the request as a customer (should be forbidden)
        mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    public void testGetBook() throws Exception {
        // First create a book
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Get Test Book");
        bookDTO.setAuthor("Get Test Author");
        bookDTO.setYear(2024);
        bookDTO.setGenre("Fiction");
        bookDTO.setDescription("A test book for get operation");

        // Create the book
        MvcResult createResult = mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Extract the book ID from the response
        BookDTO createdBook = objectMapper.readValue(createResult.getResponse().getContentAsString(), BookDTO.class);
        Integer bookId = createdBook.getId();

        // Get the book
        MvcResult getResult = mockMvc.perform(get("/api/v1/books/" + bookId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // Verify the response
        String responseBody = getResult.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Get Test Book"), "Response should contain the book title");
        assertTrue(responseBody.contains("Get Test Author"), "Response should contain the author");
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    public void testGetAllBooksByGenre() throws Exception {
        // First create a book
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Genre Test Book");
        bookDTO.setAuthor("Genre Test Author");
        bookDTO.setYear(2024);
        bookDTO.setGenre("Mystery");
        bookDTO.setDescription("A test book for genre search");

        // Create the book
        mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk());

        // Get books by genre
        MvcResult getResult = mockMvc.perform(get("/api/v1/books")
                .param("genre", "Mystery"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // Verify the response
        String responseBody = getResult.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Genre Test Book"), "Response should contain the book title");
        assertTrue(responseBody.contains("Mystery"), "Response should contain the genre");
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    public void testUpdateBook() throws Exception {
        // First create a book
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Update Test Book");
        bookDTO.setAuthor("Update Test Author");
        bookDTO.setYear(2024);
        bookDTO.setGenre("Science Fiction");
        bookDTO.setDescription("A test book for update operation");

        // Create the book
        MvcResult createResult = mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Extract the book ID from the response
        BookDTO createdBook = objectMapper.readValue(createResult.getResponse().getContentAsString(), BookDTO.class);
        Integer bookId = createdBook.getId();

        // Update the book
        createdBook.setTitle("Updated Test Book");
        createdBook.setYear(2025);

        // Perform the update
        MvcResult updateResult = mockMvc.perform(put("/api/v1/books/" + bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdBook)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // Verify the response
        String responseBody = updateResult.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Updated Test Book"), "Response should contain the updated title");
        assertTrue(responseBody.contains("2025"), "Response should contain the updated year");
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    public void testDeleteBook() throws Exception {
        // First create a book
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Delete Test Book");
        bookDTO.setAuthor("Delete Test Author");
        bookDTO.setYear(2024);
        bookDTO.setGenre("Thriller");
        bookDTO.setDescription("A test book for delete operation");

        // Create the book
        MvcResult createResult = mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Extract the book ID from the response
        BookDTO createdBook = objectMapper.readValue(createResult.getResponse().getContentAsString(), BookDTO.class);
        Integer bookId = createdBook.getId();

        // Delete the book
        mockMvc.perform(delete("/api/v1/books/" + bookId))
                .andDo(print())
                .andExpect(status().isNoContent());

        // Verify the book is deleted by trying to get it
        MvcResult getResult = mockMvc.perform(get("/api/v1/books/" + bookId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        
        // Debug: Print the actual response
        String responseBody = getResult.getResponse().getContentAsString();
        System.out.println("Response after deletion: " + responseBody);
        
        // Parse the response and check the deleted field
        BookDTO deletedBook = objectMapper.readValue(responseBody, BookDTO.class);
        assertTrue(deletedBook.getDeleted(), "Book should be marked as deleted");
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void testDeleteBookUnauthorized() throws Exception {
        // First create a book as admin
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Unauthorized Delete Test Book");
        bookDTO.setAuthor("Unauthorized Delete Test Author");
        bookDTO.setYear(2024);
        bookDTO.setGenre("Romance");
        bookDTO.setDescription("A test book for unauthorized delete operation");

        // Create the book as admin
        MvcResult createResult = mockMvc.perform(post("/api/v1/books")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMINISTRATOR"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Extract the book ID from the response
        BookDTO createdBook = objectMapper.readValue(createResult.getResponse().getContentAsString(), BookDTO.class);
        Integer bookId = createdBook.getId();

        // Try to delete the book as a customer (should be forbidden)
        mockMvc.perform(delete("/api/v1/books/" + bookId))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
} 