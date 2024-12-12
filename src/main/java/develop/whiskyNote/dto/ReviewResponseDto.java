package develop.whiskyNote.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewResponseDto {
    private String content;
    private Map<Long, String> imageUrl;
    private Boolean isAnonymous;
    private List<String> tags;
    private LocalDate openDate;
    private Long score;
    @Override
    public String toString() {
        return new StringJoiner(", ", ReviewResponseDto.class.getSimpleName() + "[", "]")
                .add("content='" + content + "'")
                .add("imageUrl='" + imageUrl + "'")
                .add("isAnonymous='" + isAnonymous + "'")
                .add("tags='" + tags + "'")
                .add("openDate='" + openDate + "'")
                .add("score='" + score + "'")
                .toString();
    }
    @JsonProperty("is_anonymous")
    public boolean getIsAnonymous() {
        return isAnonymous;
    }
}
