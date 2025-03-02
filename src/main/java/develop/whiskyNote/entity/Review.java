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
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

@Builder
@Entity
@Getter
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_whisky_uuid", nullable = false)
    private UserWhisky userWhisky;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_uuid", nullable = false)
    private User user;

    @Column(name = "content")
    private String content;

    @Column(name = "image_names")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> imageNames;

    @Column(name="is_anonymous")
    private Boolean isAnonymous;

    @Column(name="open_date")
    private LocalDate openDate;

    @Column(name = "tags")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> tags;

    @Column(name="score")
    private Long score;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "mod_date")
    private LocalDateTime modDate;

    @Override
    public String toString() {
        return new StringJoiner(", ", Review.class.getSimpleName() + "[", "]")
                .add("uuid='" + uuid + "'")
                .add("user_device='" + user.getDeviceId() + "'")
                .add("content='" + content + "'")
                .add("isAnonymous='" + isAnonymous + "'")
                .add("imageNames='" + imageNames + "'")
                .add("openDate='" + openDate + "'")
                .add("tags='" + tags + "'")
                .add("score='" + score + "'")
                .add("regDate='" + regDate + "'")
                .add("modDate='" + modDate + "'")
                .toString();
    }
}
