package backend2.business.book;

import backend2.domain.BookDTO;
import backend2.persistence.BookRepository;
import backend2.business.mapper.BookMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAllBooksByGenreUseCase {
    
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Transactional
    public List<BookDTO> getAllBooksByGenre(String genre) {
        return bookRepository.findByGenre(genre)
                .stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }
}