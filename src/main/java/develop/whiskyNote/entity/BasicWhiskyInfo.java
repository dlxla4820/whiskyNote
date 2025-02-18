package develop.whiskyNote.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@Entity
@Getter
@Table(
        name = "basic_whisky_information",
        indexes = {
                @Index(name = "idx_name_sort", columnList = "korea_name, english_name")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BasicWhiskyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    @Column(name = "korea_name", nullable = false)
    private String koreaName;

    @Column(name="english_name", nullable = false)
    private String englishName;

    @Column(name="country", nullable = false)
    private String country;



}
