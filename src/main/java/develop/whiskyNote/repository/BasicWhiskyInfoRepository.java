package develop.whiskyNote.repository;

import develop.whiskyNote.entity.BasicWhiskyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BasicWhiskyInfoRepository extends JpaRepository<BasicWhiskyInfo, UUID> {
}
