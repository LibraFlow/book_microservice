package backend2.business.book;

import backend2.persistence.entity.BookEntity;
import backend2.domain.BookDTO;
import backend2.persistence.BookRepository;
import backend2.business.mapper.BookMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.owasp.encoder.Encode;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AddBookUseCase {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final Logger logger = LoggerFactory.getLogger(AddBookUseCase.class);

    @Transactional
    public BookDTO addBook(BookDTO bookDto) {
        if (bookDto == null) {
            throw new IllegalArgumentException("Book DTO cannot be null");
        }
        BookEntity bookEntity = bookMapper.toEntity(bookDto);
        BookEntity savedBook = bookRepository.save(bookEntity);
        // Audit log: bookId and timestamp
        logger.info("AUDIT: Book added - bookId={}, timestamp={}", savedBook.getId(), java.time.Instant.now());
        return bookMapper.toDTO(savedBook);
    }
}