package develop.whiskyNote.repository;

import develop.whiskyNote.entity.Whisky;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WhiskyRepository extends JpaRepository<Whisky, UUID> {
    //jpa로 검색 결과가 존재하는지만 빠르게 확인
    boolean existsByKoreaNameContainingIgnoreCase(String keyword);
    boolean existsByEnglishNameContainingIgnoreCase(String keyword);
}
