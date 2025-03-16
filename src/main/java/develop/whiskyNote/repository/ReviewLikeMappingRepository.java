package develop.whiskyNote.repository;

import develop.whiskyNote.entity.ReviewLikeMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeMappingRepository extends JpaRepository<ReviewLikeMapping, Long> {
}
