package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ToString
public class MyWhiskyListResponseDto {
    private UUID whiskyUuid;
    private String koreaName;
    private String englishName;
    private Double score;
    private String country;
    private Integer bottledYear;
    private String imageName;
    private Double strength;
    private String category;
    private String caskType;
    private LocalDate openDate;
    private String memo;
    private LocalDateTime regDate;
    private LocalDateTime modDate;



}
