package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetOtherReviewListResponseDto {
    private String reviewUuid;
    private String userWhiskyUuid;
    private String content;
    private Boolean isAnonymous;
    private LocalDateTime openDate;
    private Long score;
    private List<String> tags;
    private List<String> imageNames;
    private LocalDateTime lastUpdateDate;
    private Boolean likeState;
    private Integer likeCount;
}
