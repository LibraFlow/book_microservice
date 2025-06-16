package backend2.business.mapper;

import backend2.domain.BookDTO;
import backend2.persistence.entity.BookEntity;
import org.owasp.encoder.Encode;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class BookMapper {

    public BookDTO toDTO(BookEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return BookDTO.builder()
                .id(entity.getId())
                .author(Encode.forHtml(entity.getAuthor()))
                .year(entity.getYear())
                .title(Encode.forHtml(entity.getTitle()))
                .genre(Encode.forHtml(entity.getGenre()))
                .description(Encode.forHtml(entity.getDescription()))
                .deleted(entity.getDeleted())
                .build();
    }

    public BookEntity toEntity(BookDTO dto) {
        if (dto == null) {
            return null;
        }

        return BookEntity.builder()
                .id(dto.getId())
                .author(dto.getAuthor())
                .year(dto.getYear())
                .title(dto.getTitle())
                .genre(dto.getGenre())
                .description(dto.getDescription())
                .createdAt(dto.getId() == null ? LocalDate.now() : null) // Set createdAt only for new entities
                .deleted(dto.getDeleted() != null ? dto.getDeleted() : false)
                .build();
    }
}