package develop.whiskyNote.repository;

import develop.whiskyNote.entity.ImageFile;
import develop.whiskyNote.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    void deleteByUuid(UUID uuid);
}
