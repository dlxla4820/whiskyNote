package develop.whiskyNote.repository;

import develop.whiskyNote.entity.BackupCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BackupCodeRepository extends JpaRepository<BackupCode, Long> {
    Optional<BackupCode> findFirstByUserUuidOrderByCreatedAtDesc(UUID code);
    List<BackupCode> findAllByUserUuid(UUID userUuid);
    void deleteAllByUserUuid(UUID userUUid);
}
