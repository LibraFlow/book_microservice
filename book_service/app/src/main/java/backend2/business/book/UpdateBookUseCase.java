package backend2.business.book;

import backend2.domain.BookDTO;
import backend2.persistence.entity.BookEntity;
import backend2.persistence.BookRepository;
import backend2.business.mapper.BookMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.owasp.encoder.Encode;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class UpdateBookUseCase {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final Logger logger = LoggerFactory.getLogger(UpdateBookUseCase.class);

    @Transactional
    public BookDTO updateBook(Integer id, BookDTO bookDto) {
        BookEntity existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        // Update existing entity with DTO values
        BookEntity updatedEntity = bookMapper.toEntity(bookDto);
        updatedEntity.setId(id); // Ensure we keep the same ID
        updatedEntity.setCreatedAt(existingBook.getCreatedAt()); // Preserve creation date

        BookEntity savedBook = bookRepository.save(updatedEntity);
        // Audit log: bookId and timestamp
        logger.info("AUDIT: Book updated - bookId={}, timestamp={}", savedBook.getId(), java.time.Instant.now());
        return bookMapper.toDTO(savedBook);
    }
}