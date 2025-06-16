package backend2.business.book;

import backend2.persistence.BookRepository;
import org.springframework.kafka.core.KafkaTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import backend2.persistence.entity.BookEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteBookUseCase {

    private final BookRepository bookRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public Void deleteBook(Integer id) {
        BookEntity book = bookRepository.findByIdAndDeletedFalse(id);
        if (book == null) {
            throw new IllegalArgumentException("Book not found with id: " + id);
        }
        book.setDeleted(true);
        bookRepository.save(book);
        try {
            String jsonId = objectMapper.writeValueAsString(id);
            kafkaTemplate.send("book.deleted", jsonId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize bookId for deletion event", e);
        }
        return null;
    }
}