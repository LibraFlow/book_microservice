package backend2.business.book;

import backend2.persistence.entity.BookEntity;
import backend2.domain.BookDTO;
import backend2.persistence.BookRepository;
import backend2.business.mapper.BookMapper;

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

    @Transactional
    public BookDTO addBook(BookDTO bookDto) {
        if (bookDto == null) {
            throw new IllegalArgumentException("Book DTO cannot be null");
        }
        BookEntity bookEntity = bookMapper.toEntity(bookDto);
        BookEntity savedBook = bookRepository.save(bookEntity);
        return bookMapper.toDTO(savedBook);
    }
}