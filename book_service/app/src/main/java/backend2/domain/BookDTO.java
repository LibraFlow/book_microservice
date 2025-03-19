package backend2.domain;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
public class BookDTO {
    private Integer id;
    private String author;
    private Integer year;
    private String title;
    private String genre;
    private String description;
}