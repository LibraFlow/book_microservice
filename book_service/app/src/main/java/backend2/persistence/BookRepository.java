package backend2.persistence;

import backend2.persistence.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {
    List<BookEntity> findByGenreAndDeletedFalse(String genre);
    List<BookEntity> findByDeletedFalse();
    BookEntity findByIdAndDeletedFalse(Integer id);
    List<BookEntity> findByGenreAndDeletedTrue(String genre);
}