package backend2.presentation;

import backend2.domain.BookDTO;
import backend2.business.book.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok(addBookUseCase.addBook(bookDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        deleteBookUseCase.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Integer id) {
        return ResponseEntity.ok(getBookUseCase.getBook(id));
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooksByGenre(@RequestParam String genre) {
        return ResponseEntity.ok(getAllBooksByGenreUseCase.getAllBooksByGenre(genre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Integer id, @Valid @RequestBody BookDTO bookDto) {
        return ResponseEntity.ok(updateBookUseCase.updateBook(id, bookDto));
    }
}