package develop.whiskyNote.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

@Builder
@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name="title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "image_url")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<Long, String> imageUrl;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "mod_date")
    private LocalDateTime modDate;

    @Override
    public String toString() {
        return new StringJoiner(", ", Review.class.getSimpleName() + "[", "]")
                .add("uuid='" + uuid + "'")
                .add("user_device='" + user.getDeviceId() + "'")
                .add("title='" + title + "'")
                .add("content='" + content + "'")
                .add("imageUrl='" + imageUrl + "'")
                .add("regDate='" + regDate + "'")
                .add("modDate='" + modDate + "'")
                .toString();
    }
}
