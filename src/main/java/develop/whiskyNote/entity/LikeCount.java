package develop.whiskyNote.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
@Builder
@Table(name="like_count")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LikeCount {
    @Id
    @Builder.Default
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid = UUID.randomUUID();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "review_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Review review;

    @Column(name = "like_count")
    @Builder.Default
    private int likeCount = 0;

}
