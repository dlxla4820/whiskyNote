package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.StringJoiner;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MyWhiskyListResponseDto {
    private UUID whiskyUuid;
    private String name;
    private Double score;
    private String caskType;
    private Integer releaseYear;
    private String photoUrl;
    private String strength;
    private String category;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    @Override
    public String toString() {
        return new StringJoiner(", ", MyWhiskyListResponseDto.class.getSimpleName() + "[", "]")
                .add("whiskyUuid='" + whiskyUuid + "'")
                .add("name='" + name + "'")
                .add("score='" + (score != null ? score.toString() : "null") + "'")  // Null 체크 추가
                .add("caskType='" + caskType + "'")
                .add("releaseYear='" + releaseYear + "'")
                .add("photoUrl='" + photoUrl + "'")
                .add("strength='" + strength + "'")
                .add("category='" + category + "'")
                .add("regDate='" + regDate + "'")  // regDate 수정
                .add("modDate='" + modDate + "'")  // modDate 수정
                .toString();
    }
}
