package backend2.presentation;

import backend2.domain.BookDTO;
import backend2.business.book.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
public class BookController {
    
    private final AddBookUseCase addBookUseCase;
    private final DeleteBookUseCase deleteBookUseCase;
    private final GetBookUseCase getBookUseCase;
    private final GetAllBooksByGenreUseCase getAllBooksByGenreUseCase;
    private final UpdateBookUseCase updateBookUseCase;

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdminOrLibrarian = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals("ROLE_ADMINISTRATOR") || role.equals("ROLE_LIBRARIAN"));
        if (!isAdminOrLibrarian) {
            return ResponseEntity.status(403).build();
        }
        BookDTO created = addBookUseCase.addBook(bookDto);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdminOrLibrarian = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals("ROLE_ADMINISTRATOR") || role.equals("ROLE_LIBRARIAN"));
        if (!isAdminOrLibrarian) {
            return ResponseEntity.status(403).build();
        }
        deleteBookUseCase.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Integer id) {
        BookDTO book = getBookUseCase.getBook(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooksByGenre(@RequestParam String genre) {
        return ResponseEntity.ok(getAllBooksByGenreUseCase.getAllBooksByGenre(genre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Integer id, @Valid @RequestBody BookDTO bookDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdminOrLibrarian = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals("ROLE_ADMINISTRATOR") || role.equals("ROLE_LIBRARIAN"));
        if (!isAdminOrLibrarian) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(updateBookUseCase.updateBook(id, bookDto));
    }

    /* @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleInternalServerError(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "An unexpected error occurred. Please try again later.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    } */
}