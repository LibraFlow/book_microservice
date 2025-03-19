package backend2.business.book;

import backend2.persistence.BookRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteBookUseCase {

    private final BookRepository bookRepository;

    @Transactional
    public Void deleteBook(Integer id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
        return null;
    }
}