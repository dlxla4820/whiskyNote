package develop.whiskyNote.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Entity
@Getter
@Table(name = "whisky")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Whisky {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();
    @Column(name = "whisky_name", nullable = false)
    private String whiskyName;

    @Column(name = "whisky_category")
    private String whiskyCategory;
    @Column(name = "strength")
    private String strength;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "cask_number")
    private String caskNumber;
    @Column(name = "botteled_year")
    private Integer botteledYear;
    @Column(name="user_uuid")
    private String userUuid;
}
