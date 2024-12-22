package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WhiskyListResponseDto {
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
        return new StringJoiner(", ", WhiskyListResponseDto.class.getSimpleName() + "[", "]")
                .add("whiskyUuid='" + whiskyUuid + "'")
                .add("name='" + name + "'")
                .add("score='" + score + "'")
                .add("caskType='" + caskType + "'")
                .add("releaseYear='" + releaseYear + "'")
                .add("photoUrl='" + photoUrl + "'")
                .add("strength='" + strength + "'")
                .add("category='" + category + "'")
                .add("strength='" + regDate + "'")
                .add("category='" + modDate + "'")
                .toString();
    }
}
