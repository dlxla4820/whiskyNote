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
@ToString
public class Whisky {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    @Column(name = "korea_name")
    private String koreaName;

    @Column(name = "english_name")
    private String englishName;

    @Column(name = "category")
    private String category;

    @Column(name = "strength")
    private Double strength;

    @Column(name = "country")
    private String country;
}
