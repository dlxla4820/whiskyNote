package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import develop.whiskyNote.entity.UserWhisky;
import develop.whiskyNote.entity.Whisky;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.StringJoiner;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ToString
public class WhiskyCreateRequestDto {
    private String whiskyUuid;
    private String koreaName;
    private String englishName;
    private String category;
    private Double strength;
    private String country;
    private Integer bottledYear;
    private String caskType;
    private LocalDate openDate;
    private String alias;


    public UserWhisky toUserWhisky(Whisky whisky, UUID userUuid, String imageUrl){
        return UserWhisky.builder()
                .whisky(whisky)
                .koreaName(this.koreaName)
                .englishName(this.englishName)
                .category(this.category)
                .strength(this.strength)
                .country(this.country)
                .bottledYear(this.bottledYear)
                .caskType(this.caskType)
                .openDate(this.openDate)
                .alias(this.alias)
                .imageUrl(imageUrl)
                .userUuid(userUuid)
                .regDate(LocalDateTime.now())
                .modDate(LocalDateTime.now())
                .build();
    }
}
