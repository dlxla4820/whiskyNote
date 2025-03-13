package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetOtherReviewListReqeustDto {
    private String baseWhiskyUuid;
    private String searchWord;
    private String lastIndex;
    //추천순, 별점순, 날짜순, 이름순
    private boolean likeAsc = true;
    private boolean scoreAsc = false;
    private boolean createdAtAsc = false;
    private boolean whiskyNameAsc = false;

}
