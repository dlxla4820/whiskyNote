package develop.whiskyNote.repository;

import develop.whiskyNote.entity.UserWhisky;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserWhiskyRepository extends JpaRepository<UserWhisky, UUID> {
}
