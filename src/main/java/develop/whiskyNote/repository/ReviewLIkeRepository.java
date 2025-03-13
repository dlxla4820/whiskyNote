package develop.whiskyNote.repository;

import develop.whiskyNote.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewLIkeRepository extends JpaRepository<ReviewLike, UUID> {

}
