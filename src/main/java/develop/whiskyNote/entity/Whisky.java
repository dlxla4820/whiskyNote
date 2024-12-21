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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category")
    private String category;
    @Column(name = "strength")
    private String strength;
    @Column(name = "image")
    private String image;
    @Column(name = "cask_type")
    private String caskType;
    @Column(name = "stated_age")
    private String statedAge;
    @Column(name = "cask_number")
    private String caskNumber;
    @Column(name = "year")
    private Integer year;
}
