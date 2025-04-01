package develop.whiskyNote.repository;

import develop.whiskyNote.entity.BackupCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BackupCodeRepository extends JpaRepository<BackupCode, Long> {
    Optional<BackupCode> findByCode(String code);
}
