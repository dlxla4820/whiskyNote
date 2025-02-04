package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.StringJoiner;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ToString
public class MyWhiskyListResponseDto {
    private UUID whiskyUuid;
    private String name;
    private Double score;
    private Integer releaseYear;
    private String photoUrl;
    private Double strength;
    private String category;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
