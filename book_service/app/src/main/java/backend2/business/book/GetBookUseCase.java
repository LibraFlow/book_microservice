package backend2.business.book;

import backend2.domain.BookDTO;
import backend2.persistence.BookRepository;
import backend2.business.mapper.BookMapper;
import backend2.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetBookUseCase {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Transactional
    public BookDTO getBook(Integer id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }
}