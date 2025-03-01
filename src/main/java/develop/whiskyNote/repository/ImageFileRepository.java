package develop.whiskyNote.repository;

import develop.whiskyNote.entity.ImageFile;
import develop.whiskyNote.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
    Optional<ImageFile> findByName(String name);
}
