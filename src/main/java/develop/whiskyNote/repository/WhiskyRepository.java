package develop.whiskyNote.repository;

import develop.whiskyNote.entity.Whisky;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WhiskyRepository extends JpaRepository<Whisky, UUID> {
}
