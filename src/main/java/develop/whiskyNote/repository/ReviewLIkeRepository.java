package develop.whiskyNote.repository;

import develop.whiskyNote.entity.ReviewLikeMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewLIkeRepository extends JpaRepository<ReviewLikeMapping, UUID> {

}
