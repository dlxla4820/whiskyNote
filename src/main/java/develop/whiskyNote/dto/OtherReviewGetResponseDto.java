package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OtherReviewGetResponseDto {
    private UUID reviewUuid;
    private UUID userWhiskyUuid;
    private String content;
    private Boolean isAnonymous;
    private LocalDate openDate;
    private Long score;
    private List<String> tags;
    private List<String> imageNames;
    private LocalDateTime lastUpdateDate;
    private UUID reviewLikeCountUuid;
    private Boolean likeState;
    private Integer likeCount;
}
