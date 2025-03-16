package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import develop.whiskyNote.utils.CommonUtils;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OtherReviewGetReqeustDto {
    private String baseWhiskyUuid;
    private String searchWord = "";
    private String lastIndex;
    //추천순, 별점순, 날짜순, 이름순
    private boolean likeAsc = true;
    private boolean scoreAsc = false;
    private boolean createdAtAsc = false;
    private boolean whiskyNameAsc = false;

    private boolean isKorean;

    @PostConstruct
    public void init() {
        this.isKorean = CommonUtils.containsKorean(this.searchWord);
    }

}
