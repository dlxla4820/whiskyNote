package develop.whiskyNote.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Entity
@Getter
@Table(name = "user_whisky")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class UserWhisky {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "whisky_uuid", nullable = false)
    private Whisky whisky;

    @Column(name = "korea_name")
    private String koreaName;

    @Column(name = "english_name")
    private String englishName;

    @Column(name = "category")
    private String category;

    @Column(name = "strength")
    private Double strength;

    @Column(name="country")
    private String country;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "bottled_year")
    private Integer bottledYear;

    @Column(name = "cask_type")
    private String caskType;

    @Column(name = "open_date")
    private LocalDate openDate;

    @Column(name = "memo")
    private String memo;

    @Column(name="user_uuid")
    private UUID userUuid;

    @Column(name = "last_reg_review")
    private LocalDateTime lastRegReview;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "mod_date")
    private LocalDateTime modDate;

}
