package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import develop.whiskyNote.enums.Order;
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
    private String mainSearchWord;
    private String subSearchWord;
    private boolean isMainKorean;
    private boolean isSubKorean;

    private Order likeOrder;
    private Order scoreOrder;
    private Order createdOrder;
    private Order nameOrder;

    private int page;
    private int size;

    private boolean searchFromBaseWhisky;//baseWhisky에 mainSearchWord의 결과가 존재하는지 확인
}
