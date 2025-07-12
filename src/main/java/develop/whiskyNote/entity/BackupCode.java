package develop.whiskyNote.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Builder
@Table(name = "backup_code")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class BackupCode {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "backup_code_seq")
    @SequenceGenerator(name = "backup_code_seq", sequenceName = "backup_code_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_iuid")
    private UUID userUuid;

    @Column(name = "code")
    private String code;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;
}
