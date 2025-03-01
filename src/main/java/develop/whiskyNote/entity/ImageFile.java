package develop.whiskyNote.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Builder
@Table(name = "image_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class ImageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_file_seq")
    @SequenceGenerator(name = "image_file_seq", sequenceName = "image_file_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_uuid")
    private UUID userUuid;

    @Column(name = "path", nullable = false, unique = true)
    private String path;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name="is_saved", nullable = false)
    private Boolean isSaved;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


}
